/**
 * cn.ucox.web.logservice.rendors.orcldcn.utils.DBConnKit
 *
 * @author chenw
 * @create 16/2/19.15:41
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.utils;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author chenw
 * @create 16/2/19 15:41
 * @email javacspring@gmail.com
 */
public class JDBCPool {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final JDBCPool instance = new JDBCPool();
    private static BasicDataSource dataSource = null;

    private JDBCPool() {
    }

    public static JDBCPool getInstance(String propertiesPath) {
        if (null == dataSource) {
            synchronized (instance) {
                instance.init(propertiesPath);
            }
        }
        return instance;
    }

    public static JDBCPool getInstance(Properties properties) {
        if (null == dataSource) {
            synchronized (instance) {
                instance.init(properties);
            }
        }
        return instance;
    }

    /**
     * 初始化连接池配置
     */
    private JDBCPool init(String propertiesPath) {
        Properties dbProps = new Properties();
        // 取配置文件可以根据实际的不同修改
        try {
            if (StringUtil.isBlank(propertiesPath))
                throw new RuntimeException("C3p0初始化参数配置文件未找到或路径错误!");
            dbProps.load(JDBCPool.class.getClassLoader().getResourceAsStream(propertiesPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String driveClassName = dbProps.getProperty("jdbc.driverClassName");
            String url = dbProps.getProperty("jdbc.url");
            String username = dbProps.getProperty("jdbc.username");
            String password = dbProps.getProperty("jdbc.password");

            String initialSize = dbProps.getProperty("dataSource.initialSize");
            String minIdle = dbProps.getProperty("dataSource.minIdle");
            String maxIdle = dbProps.getProperty("dataSource.maxIdle");
            String maxWait = dbProps.getProperty("dataSource.maxWait");
            String maxActive = dbProps.getProperty("dataSource.maxActive");

            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(driveClassName);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            // 初始化连接数
            if (initialSize != null)
                dataSource.setInitialSize(Integer.parseInt(initialSize));
            // 最小空闲连接
            if (minIdle != null)
                dataSource.setMinIdle(Integer.parseInt(minIdle));
            // 最大空闲连接
            if (maxIdle != null)
                dataSource.setMaxIdle(Integer.parseInt(maxIdle));
            // 超时回收时间(以毫秒为单位)
            if (maxWait != null)
                dataSource.setMaxWait(Long.parseLong(maxWait));
            // 最大连接数
            if (maxActive != null) {
                if (!maxActive.trim().equals("0"))
                    dataSource.setMaxActive(Integer.parseInt(maxActive));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建连接池失败,请检查设置!");
        }
        return instance;
    }

    /**
     * 初始化连接池配置
     */
    private JDBCPool init(Properties dbProps) {
        try {
            String driveClassName = dbProps.getProperty("jdbc.driverClassName");
            String url = dbProps.getProperty("jdbc.url");
            String username = dbProps.getProperty("jdbc.username");
            String password = dbProps.getProperty("jdbc.password");

            String initialSize = dbProps.getProperty("dataSource.initialSize");
            String minIdle = dbProps.getProperty("dataSource.minIdle");
            String maxIdle = dbProps.getProperty("dataSource.maxIdle");
            String maxWait = dbProps.getProperty("dataSource.maxWait");
            String maxActive = dbProps.getProperty("dataSource.maxActive");

            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(driveClassName);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            // 初始化连接数
            if (initialSize != null)
                dataSource.setInitialSize(Integer.parseInt(initialSize));
            // 最小空闲连接
            if (minIdle != null)
                dataSource.setMinIdle(Integer.parseInt(minIdle));
            // 最大空闲连接
            if (maxIdle != null)
                dataSource.setMaxIdle(Integer.parseInt(maxIdle));
            // 超时回收时间(以毫秒为单位)
            if (maxWait != null)
                dataSource.setMaxWait(Long.parseLong(maxWait));
            // 最大连接数
            if (maxActive != null) {
                if (!maxActive.trim().equals("0"))
                    dataSource.setMaxActive(Integer.parseInt(maxActive));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建连接池失败,请检查设置!");
        }
        return instance;
    }


    /**
     * 数据库连接
     *
     * @return Connection
     * @throws SQLException SQL异常
     */
    public Connection open() {
        if (dataSource == null) {
            throw new RuntimeException("初始化数据源失败,请调用init()方法初始化");
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("获取数据库连接异常", e);
        }
    }

    //关闭连接
    public void close(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("释放数据库连接异常", e);
            }
        }
    }

}
