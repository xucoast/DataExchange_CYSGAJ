package cn.com.oceansoft.apiservice.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Created by xuhb on 2016/11/2.
 */
public class DBAccess {
    private Connection conn;
    private Statement stmt;

    private static DBAccess access;

    public static synchronized Connection getCon(){
        //return conn;
        return ConnectionManager.getInstance().getConnection();
    }

    public static Connection getConnection(){
        return getCon();
    }

    private DBAccess(){
        connect();
    }

    private void connect(){
        conn = getCon();

        try {
//			conn.setAutoCommit(false);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void reconnect(){
        connect();
    }

    public static synchronized DBAccess getInstance(){
        if(access == null)
            access = new DBAccess();
        return access;
    }

/*	public synchronized Connection getCon(){
		return conn;
	}*/



    public synchronized ResultSet doQuery(String sql) throws SQLException{
        Connection con = getConnection();

        Statement stm = con.createStatement();
        return stm.executeQuery(sql);
    }

    public synchronized int doUpdate(String sql) throws SQLException{
        Connection con = getConnection();

        Statement stm = con.createStatement();
        int k = stm.executeUpdate(sql);

        return k;
    }

    public synchronized boolean doExcute(String sql) throws SQLException{
        Connection con = getConnection();

        Statement stm = con.createStatement();
        stm.execute(sql);
        stm.close();
        con.close();
        return true;
    }

    public synchronized void close() throws SQLException{
        if(stmt != null){
            stmt.close();
            stmt = null;
        }
        if(conn != null){
            conn.close();
            conn = null;
        }
    }
}
