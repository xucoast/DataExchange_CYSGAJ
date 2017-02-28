/**
 * 查询请求转发
 *
 * @author chenw
 * @create 16/2/19.20:37
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.processor;

import cn.com.oceansoft.apiservice.db.DBAccess;
import cn.ucox.web.framework.rendors.dcn.dispatcher.OuterNotificationDispatcher;
import cn.ucox.web.framework.rendors.dcn.entity.RespCmd;
import cn.ucox.web.framework.rendors.dcn.utils.JDBCPool;
import cn.ucox.web.framework.rendors.dcn.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Pattern;

import static cn.ucox.web.framework.rendors.dcn.utils.StringUtil.isBlank;

/**
 * Oracle DCN处理器
 *
 * @author chenw
 * @create 16/2/19 20:37
 * @email javacspring@gmail.com
 */
public class OuterCoreProcessor extends OuterNotificationDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(OuterCoreProcessor.class);

    private static final String SQL_QUERY = "SELECT SN,ACTION,RES,HASH,SUCC,CODE,MSG,RESP_TIME FROM DDX_OU_QUERY_SERVICE WHERE ROWID = ?";
    private static final String SQL_DELETE_TABLE_OU = "DELETE FROM DDX_OU_QUERY_SERVICE WHERE SN = ?";
    private static final String SQL_DELETE_TABLE_IN = "DELETE FROM DDX_IN_QUERY_SERVICE WHERE SN = ?";
    private static final String KEY_PLUGIN_PACKAGE = "dcn.plugins.package";
    private static final String KEY_PLUGIN_CLASSES = "dcn.plugins.classes";
    private static final String KEY_REQCMD_DEL = "dcn.reqcmd.del";
    private static final String KEY_REQCMD_BLACKLIST = "dcn.reqcmd.blacklist";
    private static final Pattern ACTION_PATTERN = Pattern.compile("\\d{6}");
    private static HashMap<String, IProcessor> PLUGINS = new HashMap<>();
    private static JDBCPool JDBC_POOL;
    private static Properties CONFIG_DCN_PROPERTIES;
    private static HashSet<String> CONFIG_IP_BLACKLIST = new HashSet<>();

    /**
     * 实例化处理器
     *
     * @param propertiesPath 属性文件配置地址
     */
    public OuterCoreProcessor(String propertiesPath) {
        //加载DCN配置
        CONFIG_DCN_PROPERTIES = loadDCNProperties(propertiesPath);
        JDBC_POOL = JDBCPool.getInstance(CONFIG_DCN_PROPERTIES);
        //注册插件
        registerPlugins();
        //初始化IP黑名单
//        initIPBlacklist();
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

    protected IProcessor lookupPlugin(RespCmd reqCmd) {
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
    protected RespCmd loadReqCmd(String rowId) {
        //Connection connection = JDBC_POOL.open();
        Connection connection = DBAccess.getInstance().getConnection();
        ResultSet resultSet = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement(SQL_QUERY);
            pstmt.setString(Integer.valueOf("1"), rowId);
            resultSet = pstmt.executeQuery();
            if (null != resultSet && resultSet.next()) {
                return RespCmdMaker.make(resultSet);
            } else {
                logger.error("监听数据:[{}] 加载数据失败 错误:数据为空!", rowId);
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
            //JDBC_POOL.close(connection);
        }
        return null;
    }

    protected void doPersistence(RespCmd reqCmd) {
        //Connection connection = JDBC_POOL.open();
        Connection connection = DBAccess.getInstance().getConnection();
        PreparedStatement statementDelRecIN = null;
        PreparedStatement statementDelRecOU = null;
        try {
            if (connection.getAutoCommit())
                connection.setAutoCommit(false);
            if (null != reqCmd) {
                //删除记录
                String isDelReqCmd = CONFIG_DCN_PROPERTIES.getOrDefault(KEY_REQCMD_DEL, "true").toString().trim();
                if (StringUtil.isBlank(isDelReqCmd) || isDelReqCmd.equalsIgnoreCase("true") || !isDelReqCmd.equalsIgnoreCase("false")) {
                    statementDelRecOU = connection.prepareStatement(SQL_DELETE_TABLE_OU);
                    statementDelRecOU.setString(1, reqCmd.getSerialNum());
                    int rt_ou = statementDelRecOU.executeUpdate();
                    if (1 == rt_ou) {
                        logger.error("SN:[{}] 删除表DDX_OU_QUERY_SERVICE记录成功!", reqCmd.getSerialNum());
                    }
                    statementDelRecIN = connection.prepareStatement(SQL_DELETE_TABLE_IN);
                    statementDelRecIN.setString(1, reqCmd.getSerialNum());
                    int rt_in = statementDelRecIN.executeUpdate();
                    if (1 == rt_in) {
                        logger.error("SN:[{}] 删除表DDX_IN_QUERY_SERVICE记录成功!", reqCmd.getSerialNum());
                    }
                }
            } else {
                logger.error("请求响应失败,错误:返回值为空!", reqCmd.getSerialNum());
            }
            connection.commit();
        } catch (Exception e) {
            logger.error("SN:[{}] 保存结果失败,错误:{}", reqCmd.getSerialNum(), e.getMessage());
        } finally {
            if (statementDelRecIN != null) {
                try {
                    statementDelRecIN.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statementDelRecOU != null) {
                try {
                    statementDelRecOU.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            //JDBC_POOL.close(connection);
        }
    }

    @Override
    protected void resetReqCmd() {
        RespCmdMaker.reset();
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
                in.close();
            } catch (IOException e) {
                logger.error("读取DCN配置文件关闭文件流失败!,错误:{}", e.getMessage());
            }
        }
    }


    /**
     * 命令对象复用
     */
    private static class RespCmdMaker {
        private static RespCmd cmd = new RespCmd();

        public static RespCmd make(ResultSet rs) throws SQLException {
            cmd.setSerialNum(rs.getString(1));
            cmd.setAction(rs.getString(2));
            String result = rs.getString(3);
            String hash = rs.getString(4);
            //Hash校验 计算传入查询命令Hash,未通过校验数据放弃查询
//            if (!StringKit.md5Hex(result).equalsIgnoreCase(hash)) {
//                logger.error("数据[{}]命令参数Hash校验失败!", cmd.getSerialNum());
//                return null;
//            }
            cmd.setResult(result);
            cmd.setSucc(rs.getString(5).equalsIgnoreCase("Y"));
            cmd.setCode(rs.getString(6));
            cmd.setMsg(rs.getString(7));
            cmd.setRespTime(rs.getTimestamp(8));
            return cmd;
        }

        public static void reset() {
            cmd.setSerialNum(null);
            cmd.setAction(null);
            cmd.setResult(null);
            cmd.setSucc(false);
            cmd.setCode(null);
            cmd.setRespTime(null);
        }
    }
}
