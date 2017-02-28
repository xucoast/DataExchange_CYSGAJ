package cn.com.oceansoft.apiservice.dao;

import cn.com.oceansoft.apiservice.entity.RealNameAuthInfo;
import cn.ucox.web.framework.rendors.dcn.entity.RespCmd;
import cn.ucox.web.framework.rendors.dcn.utils.JDBCPool;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * cn.com.oceansoft.apiservice.dao
 * Created by smc
 * date on 2016/3/10.
 * Email:sunmch@163.com
 */
public class QueryDao {
    private static final Logger log = LoggerFactory.getLogger(QueryDao.class);
    private static QueryDao emsDBDao = null;
    private static JDBCPool jdbcPool = null;

    public QueryDao() {
    }

    public static QueryDao getInstance(String properties) {
        if (emsDBDao == null) {
            synchronized (QueryDao.class) {
                emsDBDao = new QueryDao();
                jdbcPool = JDBCPool.getInstance(properties);
            }
        }
        return emsDBDao;
    }

    public void updateMember(RespCmd respCmd, String guid, Connection connection) {

        //Connection connection = jdbcPool.open();
        try {
            connection.setAutoCommit(false);
            RealNameAuthInfo realNameAuthInfo = JSON.parseObject(respCmd.getResult(), RealNameAuthInfo.class);
            String pasc_members_mange = "select guid from  pasc_members_mange where guid = ?";
            PreparedStatement pstmt = connection.prepareStatement(pasc_members_mange);
            pstmt.setString(1, guid);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet != null && resultSet.next()) {
                String deleteSql = "delete from pasc_members_mange where guid = ? ";
                pstmt = connection.prepareStatement(deleteSql);
                pstmt.setString(1, guid);
                pstmt.execute();
            }
            System.out.println("222");
            String insertSql = "insert into pasc_members_mange(guid,IDENTITYSTATUS,ISENABLED,ISCHECK,Temp1,Temp2) values(?,?,?,?,?,?)";
            if ("Y".equals(realNameAuthInfo.getExist())) {
                pstmt = connection.prepareStatement(insertSql);
                pstmt.setString(1,guid);
                pstmt.setInt(2, 1);
                pstmt.setInt(3,0);
                pstmt.setInt(4,0);
                pstmt.setString(5, realNameAuthInfo.getSspcs());
                pstmt.setString(6, realNameAuthInfo.getSjgsdwmc());
            } else {
                pstmt = connection.prepareStatement(insertSql);
                pstmt.setString(1,guid);
                pstmt.setInt(2, -9);
                pstmt.setInt(3,0);
                pstmt.setInt(4,0);
                pstmt.setString(5, realNameAuthInfo.getSspcs());
                pstmt.setString(6, realNameAuthInfo.getSjgsdwmc());
            }
            pstmt.execute();
            connection.commit();
            pstmt.close();
            System.out.println("333");
        } catch (Exception ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                log.error("执行pasc_members_mange表操作失败，失败信息: ", e);
                e.printStackTrace();
            }
            log.error("更新状态失败,失败信息： ", ex);
        }
    }

    public Map getWfyy(String code) {
        Map<String,Object> map = new HashMap<>();
        Connection connection = jdbcPool.open();
        String jtwfnr = null;
        try {
            String jtwfSql = "select JTWFNR,KF from T_JTWF_EX where JTWFXWDM = ?";
            PreparedStatement pstmt = connection.prepareStatement(jtwfSql);
            pstmt.setString(1, code);
            ResultSet resultSet = pstmt.executeQuery();
            if (null != resultSet && resultSet.next()) {
                jtwfnr = resultSet.getString(1);
                int kf = resultSet.getInt(2);
                map.put("jtwfnr",jtwfnr);
                map.put("kf",kf);
            }
        } catch (Exception ex) {
            log.error("查询违章内容失败: ", ex);
        } finally {
            jdbcPool.close(connection);
        }
        return map;
    }


}
