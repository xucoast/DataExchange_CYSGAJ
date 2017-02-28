/**
 * 查询请求转发
 *
 * @author chenw
 * @create 16/2/19.20:37
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.processor;

import cn.ucox.web.framework.rendors.dcn.dispatcher.InnerNotificationDispatcher;
import cn.ucox.web.framework.rendors.dcn.entity.ReqCmd;
import cn.ucox.web.framework.rendors.dcn.entity.RespCmd;
import cn.ucox.web.framework.rendors.dcn.utils.JDBCPool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Pattern;

import static cn.ucox.web.framework.rendors.dcn.utils.StringUtil.isBlank;
import static cn.ucox.web.framework.rendors.dcn.utils.StringUtil.md5Hex;

/**
 * Oracle DCN处理器
 *
 * @author chenw
 * @create 16/2/19 20:37
 * @email javacspring@gmail.com
 */
public class InnerCoreProcessor extends InnerNotificationDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(InnerCoreProcessor.class);

    private static final String SQL_QUERY = "SELECT SN,ACTION,ASYNC,COMMAND,HASH,Q_START,Q_IP,Q_TIMEOUT FROM DDX_IN_QUERY_SERVICE WHERE ROWID = ?";
    private static final String SQL_INSERT = "INSERT INTO DDX_OU_QUERY_SERVICE (SN,ACTION,RES,HASH,SUCC,CODE,MSG,RESP_TIME) VALUES(?,?,?,?,?,?,?,SYSDATE)";
    private static final String SQL_DELETE = "DELETE FROM DDX_IN_QUERY_SERVICE WHERE SN = ?";
    private static final String KEY_PLUGIN_PACKAGE = "dcn.plugins.package";
    private static final String KEY_PLUGIN_CLASSES = "dcn.plugins.classes";
    private static final String KEY_REQCMD_DEL = "dcn.reqcmd.del";
    private static final String KEY_REQCMD_BLACKLIST = "dcn.reqcmd.blacklist";
    private static final Pattern ACTION_PATTERN = Pattern.compile("\\d{6}");
    // 违章查询
    private static final String ACTION_WZCX = "010102";
    private static HashMap<String, IProcessor> PLUGINS = new HashMap<>();
    private static JDBCPool JDBC_POOL;
    private static Properties CONFIG_DCN_PROPERTIES;
//    private static HashMap<String,Object> CONFIG_DCN_PROPERTIES;
    private static HashSet<String> CONFIG_IP_BLACKLIST = new HashSet<>();

    /**
     * 实例化处理器
     *
     * @param propertiesPath 属性文件配置地址
     */
    public InnerCoreProcessor(String propertiesPath) {
        //加载DCN配置
        CONFIG_DCN_PROPERTIES = loadDCNProperties(propertiesPath);
        JDBC_POOL = JDBCPool.getInstance(CONFIG_DCN_PROPERTIES);
        //注册插件
        registerPlugins();
        //初始化IP黑名单
        initIPBlacklist();
    }


    /**
     * 初始化黑名单
     */
    private void initIPBlacklist() {
        String ips = CONFIG_DCN_PROPERTIES.getProperty(KEY_REQCMD_BLACKLIST);
        if (!isBlank(ips)) {
            String[] _ips = ips.split(",");
            Collections.addAll(CONFIG_IP_BLACKLIST, _ips);
            logger.debug("初始化黑名单成功!");
        }
    }

    /**
     * 加载DCN属性配置文件<br>
     * 此处会将配置传播至AbsDCNActionDispatcher
     *
     * @param propertiesPath 配置文件路径
     * @return Properties
     */
    private Properties loadDCNProperties(String propertiesPath) {
        if (null == propertiesPath || 0 == propertiesPath.trim().length())
            throw new RuntimeException("加载DCN配置文件失败!");
        Properties props = new Properties();
        InputStream in = getClass().getResourceAsStream(propertiesPath);
        try {
            props.load(in);
            return props;
        } catch (IOException e) {
            logger.error("读取DCN配置文件[{}]失败,错误:{}", propertiesPath, e.getMessage());
            throw new RuntimeException("加载DCN配置文件错误!", e);
        } finally {
            try {
                if (null != in)
                    in.close();
            } catch (IOException e) {
                logger.error("读取DCN配置文件关闭文件流失败!,错误:{}", e.getMessage());
            }
        }
    }


    /**
     * 加载DCN配置文件,实现此回调主要目的是为了向NotificationDispatcher注册配置属性
     *
     * @return Properties DCN配置属性
     */
    @Override
    protected Properties getProperties() {
        return CONFIG_DCN_PROPERTIES;
    }

    /**
     * 注册插件
     */
    private void registerPlugins() {
        //插件包名
        String plugin_package = CONFIG_DCN_PROPERTIES.getOrDefault(KEY_PLUGIN_PACKAGE, "").toString();
        if (null == plugin_package || 0 == plugin_package.trim().length())
            throw new RuntimeException("读取配置文件插件包路径配置[" + KEY_PLUGIN_PACKAGE + "]失败 错误:路径不存在或为空!");
        //插件类名
        String pluginClassStr = CONFIG_DCN_PROPERTIES.getProperty(KEY_PLUGIN_CLASSES);
        if (null == pluginClassStr || 0 == pluginClassStr.trim().length() || 0 == pluginClassStr.split(",").length)
            throw new RuntimeException("读取配置文件已注册插件配置[" + KEY_PLUGIN_PACKAGE + "]失败 错误:插件类名不存在或为空!");
        String[] pluginClass = pluginClassStr.split(",");
        for (String clazz : pluginClass) {
            String pluginFullName = String.format("%s.%s", plugin_package, clazz);
            try {
                IProcessor plugin = (IProcessor) (Class.forName(pluginFullName).newInstance());
                String command = plugin.command();
                if (null != command && ACTION_PATTERN.matcher(command).matches()) {
                    PLUGINS.put(plugin.command(), plugin);
                    logger.info("插件[{}]注册成功,插件支持命令标识[{}]!", pluginFullName, plugin.command());
                } else {
                    logger.error("插件[{}]注册失败,插件支持命令标识[{}]不正确!", pluginFullName, plugin.command());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("插件[{}]注册失败,错误:实例化失败!", pluginFullName);
            } catch (ClassNotFoundException e) {
                logger.error("插件[{}]注册失败,错误:类未找到!", pluginFullName);
            }
        }
    }

    protected IProcessor lookupPlugin(ReqCmd reqCmd) {
        String action = reqCmd.getAction();
        if (null != action && ACTION_PATTERN.matcher(reqCmd.getAction()).matches()) {
            if (PLUGINS.containsKey(action))
                return PLUGINS.get(action);
            logger.error("命令:[{}] 加载插件失败,未找到符合当前命令插件!", action);
            return null;
        } else {
            logger.error("命令:[{}] 加载插件失败,当前插件命令标识不正确!", action);
            return null;
        }
    }

    @Override
    protected ReqCmd loadCommand(String rowId) {
        Connection connection = JDBC_POOL.open();
        ResultSet resultSet = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement(SQL_QUERY);
            pstmt.setString(Integer.parseInt("1"), rowId);
            resultSet = pstmt.executeQuery();
            if (null != resultSet && resultSet.next()) {
                return ReqCmdMaker.make(resultSet);
            } else {
                logger.error("通知:[{}] 加载通知数据失败 错误:数据为空!", rowId);
            }
            pstmt.close();
        } catch (SQLException e) {
            logger.error("加载数据{}失败,错误:{}", rowId, e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error("关闭数据结果集失败,错误:{}", e.getMessage());
            }
            JDBC_POOL.close(connection);
        }
        return null;
    }

    protected void doPersistence(ReqCmd reqCmd, RespCmd respCmd) {
        logger.debug("SN:[{}] 开始保存查询结果", reqCmd.getSerialNum());
        if (null == respCmd) {
            logger.error("流水号:[{}] 请求响应成功,返回值为空!", reqCmd.getSerialNum());
            return;
        }
        Connection connection = JDBC_POOL.open();
        PreparedStatement statementInsert = null;
        PreparedStatement statementDel = null;
        Statement stmt = null;
        try {
            if (connection.getAutoCommit())
                connection.setAutoCommit(false);
            if (null != respCmd) {
                //回写输入数据库
                statementInsert = connection.prepareStatement(SQL_INSERT);
                String sn = respCmd.getSerialNum();
                if (isBlank(sn)) {
                    logger.error("命令:[{}] 请求流水号缺失!", reqCmd.getAction());
                    return;
                }
                String action = respCmd.getAction();
                if (isBlank(action)) {
                    logger.error("SN:[{}] 请求服务返回数据命令为空!", reqCmd.getSerialNum());
                    return;
                }
//                String result = respCmd.getResult();
//                if (isBlank(result)) {
//                    logger.error("SN:[{}] 请求服务返回数据结果为空!", respCmd.getSerialNum());
//                    return;
//                }
                // 违章查询，结果写入新表。每条违章对应一条记录
                // 修复webService问题时，此处提交sql都修改为addBatch方式
                if(ACTION_WZCX.equals(respCmd.getAction())){
                    stmt = connection.createStatement();

                    String wzxx = respCmd.getResult();
                    JSONArray jsonObject = JSON.parseArray(wzxx);
                    String md5 = md5Hex(null == respCmd.getResult() ? "" : respCmd.getResult());
                    String flag = respCmd.isSucc() ? "Y" : "N";
                    // 原ou表插入一条记录
                    String ouSql = "INSERT INTO DDX_OU_QUERY_SERVICE (SN,ACTION,RES,HASH,SUCC,CODE,MSG,RESP_TIME) "
                            +"VALUES('"+respCmd.getSerialNum()+"','"+respCmd.getAction()
                            +"',null,'"+md5+"','"+flag+"','"+respCmd.getCode()
                            +"','"+respCmd.getMsg()+"',SYSDATE)";
                    stmt.addBatch(ouSql);
                    if(jsonObject!=null){
                        for(int i = 0; i < jsonObject.size(); i++){
                            String wzResult = jsonObject.get(i).toString();
                            JSONObject jsonObj = JSONObject.parseObject(wzResult);
                            StringBuffer sql = new StringBuffer("INSERT INTO DDX_OU_QUERY_SERVICE_ATT (");
                            sql.append("GUID,SN,ACTION,RES,HASH,SUCC,CODE,MSG,VIOLATE_TIME) VALUES(sys_guid(),");
                            sql.append("'").append(respCmd.getSerialNum()).append("',");
                            sql.append("'").append(respCmd.getAction()).append("',");
                            sql.append("'").append(wzResult).append("',");
                            sql.append("'").append(md5).append("',");
                            sql.append("'").append(flag).append("',");
                            sql.append("'").append(respCmd.getCode()).append("',");
                            sql.append("'").append(respCmd.getMsg()).append("',");
                            String wfsj = "9999-12-31 23:59:59";
                            if(jsonObj.get("wfsj")!=null && jsonObj.get("wfsj").toString().length()>18){
                                wfsj = jsonObj.get("wfsj").toString().substring(0,jsonObj.get("wfsj").toString().lastIndexOf("."));
                            }
                            sql.append("to_date('").append(wfsj).append("','yyyy-mm-dd hh24:mi:ss'))");
                            stmt.addBatch(sql.toString());
                        }
                    }else{
                        StringBuffer sql = new StringBuffer("INSERT INTO DDX_OU_QUERY_SERVICE_ATT (");
                        sql.append("GUID,SN,ACTION,RES,HASH,SUCC,CODE,MSG,VIOLATE_TIME) VALUES(sys_guid(),");
                        sql.append("'").append(respCmd.getSerialNum()).append("',");
                        sql.append("'").append(respCmd.getAction()).append("',");
                        sql.append("null,");
                        sql.append("'").append(md5).append("',");
                        sql.append("'").append(flag).append("',");
                        sql.append("'").append(respCmd.getCode()).append("',");
                        sql.append("'").append(respCmd.getMsg()).append("',");
                        sql.append("SYSDATE)");
                        stmt.addBatch(sql.toString());
                    }

                    stmt.executeBatch();
                }else{
                    statementInsert.setString(1, respCmd.getSerialNum());
                    statementInsert.setString(2, respCmd.getAction());
                    statementInsert.setString(3, respCmd.getResult());
                    statementInsert.setString(4, md5Hex(null == respCmd.getResult() ? "" : respCmd.getResult()));
                    statementInsert.setString(5, respCmd.isSucc() ? "Y" : "N");
                    statementInsert.setString(6, respCmd.getCode());
                    statementInsert.setString(7, respCmd.getMsg());
                    if (0 == statementInsert.executeUpdate()) {
                        logger.error("SN:[{}] 保存接口返回失败!", respCmd.getSerialNum());
                    } else {
                        logger.debug("SN:[{}] 保存数据成功!", respCmd.getSerialNum());
                    }
                }

                //删除记录
                String isDelReqCmd = CONFIG_DCN_PROPERTIES.getOrDefault(KEY_REQCMD_DEL, "true").toString().trim();
                if (isDelReqCmd.trim().length() == 0 || isDelReqCmd.equalsIgnoreCase("true") || !isDelReqCmd.equalsIgnoreCase("false")) {
                    statementDel = connection.prepareStatement(SQL_DELETE);
                    statementDel.setString(1, respCmd.getSerialNum());
                    int rt = statementDel.executeUpdate();
                    if (1 == rt) {
                        logger.error("流水号:[{}] 删除ReqCmd记录成功!", respCmd.getSerialNum());
                    }
                }
            } else {
                logger.error("流水号:[{}] 请求响应成功,返回值为空!", reqCmd.getSerialNum());
            }
            connection.commit();
        } catch (Exception e) {
            logger.error("流水号:[{}] 保存结果失败,错误:{}", respCmd.getSerialNum(), e.getMessage());
        } finally {
            if (statementInsert != null) {
                try {
                    statementInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statementDel != null) {
                try {
                    statementDel.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            JDBC_POOL.close(connection);
        }
    }

    @Override
    protected void resetReqCmd() {
        ReqCmdMaker.reset();
    }


    /**
     * 命令对象复用
     */
    private static class ReqCmdMaker {

        public static ReqCmd make(ResultSet rs) throws SQLException {
            ReqCmd cmd = new ReqCmd();
            cmd.setSerialNum(rs.getString(1));
            cmd.setAction(rs.getString(2));
            boolean async = rs.getString(3).equalsIgnoreCase("Y");
            String data = rs.getString(4);
            String hash = rs.getString(5);
            Date start = rs.getDate(6);
            String ip = rs.getString(7);
            long ttl = rs.getLong(8);

            //超时校验
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            if (!async && ((cal.getTimeInMillis() + ttl * 1000) < System.currentTimeMillis())) {
                logger.error("数据[{}] 同步请求超时,中止查询!", cmd.getSerialNum());
                return null;
            }
            //IP校验 黑名单用户停止查询
            if (CONFIG_IP_BLACKLIST.contains(ip)) {
                logger.error("数据[{}] 当前IP[{}]请求已列入黑名单,中止查询!", cmd.getSerialNum(), ip);
                return null;
            }
            //Hash校验 计算传入查询命令Hash,未通过校验数据放弃查询
//            if (!md5Hex(data).equalsIgnoreCase(hash)) {
//                logger.error("数据[{}] 参数Hash校验失败,中止查询!", cmd.getSerialNum());
//                return null;
//            }
            cmd.setAsync(async);
            cmd.setCommand(data);
            cmd.setHash(hash);
            cmd.setReqStart(start);
            cmd.setReqIP(ip);
            cmd.setReqTimeout(ttl);
            return cmd;
        }

        public static void reset() {
//            cmd.setSerialNum(null);
//            cmd.setAction(null);
//            cmd.setCommand(null);
//            cmd.setHash(null);
//            cmd.setReqStart(null);
//            cmd.setReqIP(null);
//            cmd.setReqTimeout(0L);
        }
    }
}
