package cn.com.oceansoft.apiservice.plugins;

import cn.com.oceansoft.apiservice.db.DBAccess;
import cn.com.oceansoft.apiservice.db.JjConnection;
import cn.com.oceansoft.apiservice.entity.DriverLicenseSearchCondition;
import cn.ucox.web.framework.rendors.dcn.entity.ReqCmd;
import cn.ucox.web.framework.rendors.dcn.entity.RespCmd;
import cn.ucox.web.framework.rendors.dcn.processor.IProcessor;
import cn.ucox.web.framework.rendors.dcn.processor.InnerCoreProcessor;
import cn.ucox.web.framework.rendors.dcn.utils.JDBCPool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * cn.com.oceansoft.apiservice.plugins
 * Created by smc
 * date on 2016/3/2.
 * Email:sunmch@163.com
 * 驾照查询插件
 */
public class DriverLicenseCheckPlugn implements IProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DriverLicenseCheckPlugn.class);

    //private String jszjfSql = "select count(*) num from trff_cy.v_sj_drivinglicense@cyjj where dabh=? and sfzmhm=?";
    private String jszjfSql = "select count(*) num from trff_cy.v_sj_drivinglicense where dabh=? and sfzmhm=?";
    @Override
    public String command() {
        return "010110";
    }

    @Override
    public RespCmd doQuery(ReqCmd reqCmd) {
        logger.debug("驾驶证校验,线程:[{}],请求对象:{}", Thread.currentThread(), JSON.toJSONString(reqCmd));
        RespCmd respCmd = new RespCmd();
        DriverLicenseSearchCondition driverLicenseSearchCondition = JSON.parseObject(reqCmd.getCommand(), DriverLicenseSearchCondition.class);
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        JDBCPool jdbcPool = null;
        Connection connection = null;
        Connection connectionJj = null;
        try {
            //InnerCoreProcessor innerCoreProcessor = new InnerCoreProcessor();
            //jdbcPool = JDBCPool.getInstance("/properties/dcn-config.properties");
            //connection = jdbcPool.open();
            //connection = DBAccess.getInstance().getConnection();
            connectionJj = JjConnection.getConnection();

            respCmd.setSerialNum(reqCmd.getSerialNum());

            if(StringUtils.isBlank(driverLicenseSearchCondition.getDabh())){
                respCmd.setSucc(false);
                respCmd.setCode("1");
                respCmd.setMsg("请输入档案编号");
                return respCmd;
            }
            if(StringUtils.isBlank(driverLicenseSearchCondition.getJtglywdxsfzhm())){
                respCmd.setSucc(false);
                respCmd.setCode("1");
                respCmd.setMsg("请输入驾驶证号");
                return respCmd;
            }
            statement = connectionJj.prepareStatement(jszjfSql);
            statement.setString(1,driverLicenseSearchCondition.getDabh());
            statement.setString(2,driverLicenseSearchCondition.getJtglywdxsfzhm());
            resultSet = statement.executeQuery();

            String num = "-9";
            if (null != resultSet && resultSet.next()) {
                num = resultSet.getString("num");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("num", num);

                respCmd.setResult(jsonObject.toString());
                respCmd.setSucc(true);
                respCmd.setCode("000");
                respCmd.setMsg("查询成功");
                statement = null;
            }
            connection = DBAccess.getInstance().getConnection();
            String sql = "update pasc_members_mange set temp5=? where guid=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "0".equals(num)?"-9":"1");
            statement.setString(2, driverLicenseSearchCondition.getUserguid());
            statement.execute();
            connection.commit();
            statement.close();
            resultSet.close();
            logger.info(driverLicenseSearchCondition.getJtglywdxsfzhm()+"，驾驶证校验结束");
        }catch(Exception e){
            e.printStackTrace();
            logger.info(driverLicenseSearchCondition.getJtglywdxsfzhm()+"，驾驶证校验出错");
        }finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            //jdbcPool.close(connection);
            try {
                if(connection!=null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return respCmd;
    }

    @Override
    public void doResponse(RespCmd respCmd) {

    }

}
