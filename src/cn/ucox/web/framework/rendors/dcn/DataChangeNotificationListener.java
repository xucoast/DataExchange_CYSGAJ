/**
 * 2010(c) Copyright Oceansoft Information System Co.,LTD. All rights reserved.
 * <p>
 * Compile: JDK1.6+
 * <p>
 * 版权所有(C)：江苏欧索软件有限公司
 * <p>
 * 公司名称：江苏欧索软件有限公司
 * <p>
 * 公司地址：苏州新区青山路1号索迪实训基地
 * <p>
 * 网址: http://www.oceansoft.com.cn
 * <p>
 * 版本: 渣土车监管平台_1.0
 * <p>
 * 作者: 090922(陈伟)
 * <p>
 * <p>
 * 文件名: DataChangeNotificationListener.java
 * <p>
 * 类产生时间:2011-12-20 下午1:56:13
 * <p>
 * 负责人: 090922(陈伟)
 * <p>
 * 所在组: 渣土车监管平台
 * <p>
 * 所在部门: 开发一部-技术二部
 * <p>
 * email：javacspring@gmail.com
 * <p>
 * <p>
 */
package cn.ucox.web.framework.rendors.dcn;

import cn.ucox.web.framework.rendors.dcn.dispatcher.IDispatchable;
import cn.ucox.web.framework.rendors.dcn.processor.InnerCoreProcessor;
import cn.ucox.web.framework.rendors.dcn.processor.OuterCoreProcessor;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.dcn.RowChangeDescription;
import oracle.jdbc.dcn.TableChangeDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Oracle数据更新通知注册工具类
 *
 * @author chenw
 */
public class DataChangeNotificationListener {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String KEY_DCN_USER = "dcn.user";
    private static final String KEY_DCN_PWD = "dcn.pwd";
    private static final String KEY_DCN_URL = "dcn.url";
    private static final String KEY_DCN_NTF_LOCAL_HOST = "dcn.ntf_local_host";

    private static final String SQL_BIND_INNER_TABLE_QUERY = "SELECT * FROM DDX_IN_QUERY_SERVICE WHERE 1 = 2";
    private static final String SQL_BIND_OUTER_TABLE_QUERY = "SELECT * FROM DDX_OU_QUERY_SERVICE WHERE 1 = 2";

    private static DataChangeNotificationListener INSTANCE = new DataChangeNotificationListener();
    private Properties dcnProperties;
    private OracleConnection connection = null;
    private DatabaseChangeRegistration dcr = null;

    private DataChangeNotificationListener() {
    }

    public static DataChangeNotificationListener getInstance() {
        return INSTANCE;
    }

    public void listening(IDispatchable dispatcher) {
        //1.开始判断传入接口实现是内外分发器或外网分发器,从而构建监听表
        String listenSQL;
        if (dispatcher instanceof InnerCoreProcessor) {
            listenSQL = SQL_BIND_INNER_TABLE_QUERY;
        } else if (dispatcher instanceof OuterCoreProcessor) {
            listenSQL = SQL_BIND_OUTER_TABLE_QUERY;
        } else {
            throw new RuntimeException("初始化DCN监听器失败 错误:指定任务调度器错误!");
        }
        try {
            dcnProperties = dispatcher.getDCNProperties();
            connection = buildDCNConnection();
            dcr = connection.registerDatabaseChangeNotification(buildDCNConfig());
            dcr.addListener((event) -> {
                if (event.getRegId() != dcr.getRegId())
                    return;
                for (TableChangeDescription tableChange : event.getTableChangeDescription()) {
                    for (RowChangeDescription rcd : tableChange.getRowChangeDescription()) {
                        //仅分发INSERT动作
                        if (rcd.getRowOperation().equals(RowChangeDescription.RowOperation.INSERT))
                            dispatcher.dispatch(tableChange.getTableName(),
                                    rcd.getRowid().stringValue(),
                                    rcd.getRowOperation());
                    }
                }
            });
            initListeningTable(listenSQL, connection);
        } catch (Exception e) {
            try {
                if (null != dcr) {
                    connection.unregisterDatabaseChangeNotification(dcr);
                    logger.error("启动Oracle数据库监听失败,取消注册监听");
                }
            } catch (SQLException ex) {
                logger.error("启动Oracle数据库监听失败,错误:{}", ex.getMessage());
                throw new RuntimeException("启动数据库监听异常", e);
            }
            logger.info("启动Oracle数据库监听失败,错误:{}", e.getMessage());
            throw new RuntimeException("启动数据库监听异常", e);
        }
    }


    private void initListeningTable(String listeningSQL, OracleConnection connection) {
        Statement stmt = null;
        try {
            connection.setAutoCommit(true);
            stmt = connection.createStatement();
            ((OracleStatement) stmt).setDatabaseChangeRegistration(dcr);
            ResultSet rs = stmt.executeQuery(listeningSQL);
            while (rs.next()) {
            }
            String[] tableNames = dcr.getTables();
            for (String tableName : tableNames) {
                logger.debug("DCN开始监听{}表", tableName);
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("DCN绑定监听数据表异常", e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("关闭Statement异常", e);
            }
        }

    }

    /**
     * 注销DCN监听
     */
    public void stopListening() {
        try {
            if (null != connection && null != dcr) {
                connection.unregisterDatabaseChangeNotification(dcr);
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("关闭DCN监听失败,错误:{}", e.getMessage());
        }
    }

    /**
     * 构建DCN配置
     *
     * @return Properties
     */
    private Properties buildDCNConfig() {
        Properties dcnProperties = new Properties();
        String dncLocalHost = dcnProperties.getProperty(KEY_DCN_NTF_LOCAL_HOST);
        if (null != dncLocalHost && dncLocalHost.trim().length() > 0) {
            dcnProperties.put(OracleConnection.NTF_LOCAL_HOST, dncLocalHost);
        }
        dcnProperties.put(OracleConnection.CONNECTION_PROPERTY_AUTOCOMMIT, "true");
        dcnProperties.put(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
        dcnProperties.put(OracleConnection.NTF_QOS_RELIABLE, "true");
        dcnProperties.setProperty(OracleConnection.DCN_BEST_EFFORT, "false");
        dcnProperties.setProperty(OracleConnection.NTF_QOS_PURGE_ON_NTFN, "false");
        dcnProperties.setProperty(OracleConnection.NTF_TIMEOUT, "0");
        return dcnProperties;
    }

    /**
     * 构建DCN数据库链接
     *
     * @return OracleConnection
     * @throws SQLException
     */
    private OracleConnection buildDCNConnection() throws SQLException {
        OracleDriver driver = new OracleDriver();
        Properties prop = new Properties();
        prop.setProperty("user", dcnProperties.getProperty(KEY_DCN_USER));
        prop.setProperty("password", dcnProperties.getProperty(KEY_DCN_PWD));
        return driver.connect(dcnProperties.getProperty(KEY_DCN_URL), prop).unwrap(OracleConnection.class);
    }
}
