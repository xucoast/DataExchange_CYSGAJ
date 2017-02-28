package cn.com.oceansoft.apiservice.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import cn.ucox.web.framework.rendors.dcn.utils.StringUtil;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xuhb on 2016/11/2.
 */
public class DbcpBean {
    private static final Logger logger = LoggerFactory.getLogger(DbcpBean.class);
    private static DbcpBean instance;
    //初始化时获取10个连接
    private static String initPoolSize = "10";
    //最小空闲连接
    private static String minIdle = "10";
    //最大空闲连接
    private static String maxIdle = "50";
    //
    private static String maxWait = "5000";
    //
    private static String maxActive = "100";
    public static final DbcpBean getInstance(){
        if(instance == null){
            instance = new DbcpBean();
        }
        return instance;
    }

    /** 数据源，static */
    private static DataSource dataSource;

    /** 从数据源获得一个连接 */
    public Connection getConn() {

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.info("获得连接出错！");
            e.printStackTrace();
            return null;
        }
    }

    /** 默认的构造函数 */
    public DbcpBean() {
        initDS();
    }

    /** 构造函数，初始化了 DS ，指定 数据库
     public DbcpBean(String connectURI) {
     initDS(connectURI);
     }*/

    /** 构造函数，初始化了 DS ，指定 所有参数 */
    public DbcpBean(String connectURI, String username, String pswd, String driverClass, int initialSize,
                    int maxActive, int maxIdle, int maxWait) {
        initDS(connectURI, username, pswd, driverClass);
    }

    public static void initDS() {
        Properties dbProps = new Properties();
        // 取配置文件可以根据实际的不同修改
        try {
            if (StringUtil.isBlank("/properties/dcn-config.properties"))
                throw new RuntimeException("C3p0初始化参数配置文件未找到或路径错误!");
            dbProps.load(DbcpBean.class.getClassLoader().getResourceAsStream("/properties/dcn-config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 数据库配置
        String driveClassName = dbProps.getProperty("jdbc.driverClassName");
        String url = dbProps.getProperty("jdbc.url");
        String username = dbProps.getProperty("jdbc.username");
        String password = dbProps.getProperty("jdbc.password");
        // 连接池配置
        initPoolSize = dbProps.getProperty("dataSource.initialSize");
        minIdle = dbProps.getProperty("dataSource.minIdle");
        maxIdle = dbProps.getProperty("dataSource.maxIdle");
        maxWait = dbProps.getProperty("dataSource.maxWait");
        maxActive = dbProps.getProperty("dataSource.maxActive");
        logger.info("连接池配置项maxActive"+maxActive);

/*        String DB_PATH = ByContext.getInstance().getProperty("mysqlDB_PATH");
        String DB_USER = ByContext.getInstance().getProperty("mysqlDB_USER");
        String DB_PASS = ByContext.getInstance().getProperty("mysqlDB_PASS");
        String DB_PORT = ByContext.getInstance().getProperty("mysqlDB_PORT");
        String DB_NAME = ByContext.getInstance().getProperty("mysqlDB_NAME");
        String DB_DRIVER = "com.mysql.jdbc.Driver";*/


        String DB_URL = url;

        initDS(DB_URL, username, password, driveClassName);
    }

    /**
     * 指定所有参数连接数据源
     *
     * @param connectURI 数据库
     * @param username 用户名
     * @param pswd 密码
     * @param driverClass 数据库连接驱动名
     * @return
     */
    public static void initDS(String connectURI, String username, String pswd, String driverClass) {
        logger.info("初始化连接池");
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driverClass);
        ds.setUsername(username);
        ds.setPassword(pswd);
        ds.setUrl(connectURI);
        //初始化时获取50个连接
        ds.setInitialSize(Integer.parseInt(initPoolSize)); // 初始的连接数；
        //连接池中保留的最大连接数
        ds.setMaxActive(Integer.parseInt(maxActive));
        // 最小空闲连接
        ds.setMinIdle(Integer.parseInt(minIdle));
        // 最大空闲连接
        ds.setMaxIdle(Integer.parseInt(maxIdle));
        // 连接泄露是否打印
        ds.setLogAbandoned(true);
        // 是否自动回收超时连接
        ds.setRemoveAbandoned(true);
        // 空闲连接被逐出连接池时间（默认值300秒）
        // ds.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        // 超时等待时间（毫秒）
        ds.setMaxWait(Integer.parseInt(maxWait));
        // 在空闲连接回收器线程运行期间休眠的时间值,以毫秒为单位
        ds.setTimeBetweenEvictionRunsMillis(1000*60*5);
        // 在每次空闲连接回收器线程(如果有)运行时检查的连接数量
        ds.setNumTestsPerEvictionRun(Integer.parseInt(maxActive));

        ds.setTestWhileIdle(true);
        ds.setMinEvictableIdleTimeMillis(1000*60*30);
        ds.setTestOnBorrow(true);
        ds.setTestOnReturn(true);

        //ds.setMinEvictableIdleTimeMillis(10000);
        // 连接验证sql
        ds.setValidationQuery("SELECT 1 FROM DUAL");

        dataSource = ds;
    }

    /** 获得数据源连接状态 */
    public static Map<String, Integer> getDataSourceStats() throws SQLException {
        BasicDataSource bds = (BasicDataSource) dataSource;
        Map<String, Integer> map = new HashMap<String, Integer>(2);
        map.put("active_number", bds.getNumActive());
        map.put("idle_number", bds.getNumIdle());
        return map;
    }

    /** 关闭数据源 */
    protected static void shutdownDataSource() throws SQLException {
        BasicDataSource bds = (BasicDataSource) dataSource;
        bds.close();
    }

}
