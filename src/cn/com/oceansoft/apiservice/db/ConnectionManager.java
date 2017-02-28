package cn.com.oceansoft.apiservice.db;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * Created by xuhb on 2016/11/2.
 */
public class ConnectionManager {
    private static ConnectionManager instance;

    private static Connection con;

    public static final ConnectionManager getInstance(){
        if(instance == null){
            instance = new ConnectionManager();
        }
        return instance;
    }

    /*	private ConnectionManager(){



            String DB_PATH = ByContext.getInstance().getProperty("mysqlDB_PATH");
            String DB_USER = ByContext.getInstance().getProperty("mysqlDB_USER");
            String DB_PASS = ByContext.getInstance().getProperty("mysqlDB_PASS");
            String DB_PORT = ByContext.getInstance().getProperty("mysqlDB_PORT");
            String DB_NAME = ByContext.getInstance().getProperty("mysqlDB_NAME");
            String DB_DRIVER = "com.mysql.jdbc.Driver";


            String DB_URL = "jdbc:mysql://" + DB_PATH + ":" + DB_PORT + "/" + DB_NAME;


            try {
                ds = new ComboPooledDataSource();
                ds.setDriverClass(DB_DRIVER);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
            ds.setJdbcUrl(DB_URL);
            ds.setUser(DB_USER);
            ds.setPassword(Encodes.decrypted(DB_PASS));

            //初始化时获取50个连接
            String initPoolSize = ByContext.getInstance().getProperty("initPoolSize");
            ds.setInitialPoolSize(Integer.parseInt(initPoolSize));
            //连接池中保留的最大连接数
            String maxPoolSize = ByContext.getInstance().getProperty("maxPoolSize");
            ds.setMaxPoolSize(Integer.parseInt(maxPoolSize));
            //连接池中保留的最小连接数
            ds.setMinPoolSize(Integer.parseInt(initPoolSize));


            //当连接池中的连接耗尽的时候c3p0一次同时获取的连接数
            ds.setAcquireIncrement(30);

            //每300秒检查所有连接池中的空闲连接
            ds.setIdleConnectionTestPeriod(300);
            //最大空闲时间,6000秒内未使用则连接被丢弃
            ds.setMaxIdleTime(600);

            //定义所有连接测试都执行的测试语句
            ds.setPreferredTestQuery("select 1 from dual");
            因性能消耗大请只在需要的时候使用它。如果设为true那么在每个connection提交的
                               时候都将校验其有效性。建议使用idleConnectionTestPeriod或automaticTestTable
                               等方法来提升连接测试的性能
            ds.setTestConnectionOnCheckout(false);
            //如果设为true那么在取得连接的同时将校验连接的有效性
            ds.setTestConnectionOnCheckin(true);
            //从数据库获取新连接失败后重复尝试的次数
            ds.setAcquireRetryAttempts(3);
            //两次连接中间隔时间，单位毫秒
            ds.setAcquireRetryDelay(1000);
            获取连接失败将会引起所有等待连接池来获取连接的线程抛出异常。但是数据源仍有效
                              保留，并在下次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试
                              获取连接失败后该数据源将申明已断开并永久关闭
            ds.setBreakAfterAcquireFailure(true);
        }
        */
    public synchronized final Connection getConnection(){
        try {
            if(con == null || con.isClosed()){
                con = DbcpBean.getInstance().getConn();
            }
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized final void closeCon(){
        if(con != null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

/*	protected void finalize() throws Throwable {
		DataSources.destroy(ds); // 关闭datasource
		super.finalize();
	}*/

}
