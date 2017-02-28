package cn.com.oceansoft.apiservice.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by xuhb on 2016/11/30.
 */
public class JjConnection {
    public static Connection getConnection() throws SQLException {
        String url="jdbc:oracle:thin:@10.83.80.28:1521:dbff";
        String user="trff_sj";
        String password="trff_sj";
        Connection conn= DriverManager.getConnection(url,user,password);
        return conn;
    }

}
