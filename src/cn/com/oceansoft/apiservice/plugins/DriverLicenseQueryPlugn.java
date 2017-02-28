package cn.com.oceansoft.apiservice.plugins;

import cn.com.oceansoft.apiservice.db.DBAccess;
import cn.com.oceansoft.apiservice.db.JjConnection;
import cn.com.oceansoft.apiservice.entity.DriverLicenseInfo;
import cn.com.oceansoft.apiservice.entity.DriverLicenseSearchCondition;
import cn.com.oceansoft.apiservice.entity.OffenseVehicleInfo;
import cn.com.oceansoft.apiservice.entity.OffenseVehicleSearchCondition;
import cn.com.oceansoft.apiservice.util.RPCClientUtils;
import cn.ucox.web.framework.rendors.dcn.entity.ReqCmd;
import cn.ucox.web.framework.rendors.dcn.entity.RespCmd;
import cn.ucox.web.framework.rendors.dcn.processor.IProcessor;
import cn.ucox.web.framework.rendors.dcn.processor.InnerCoreProcessor;
import cn.ucox.web.framework.rendors.dcn.utils.JDBCPool;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * cn.com.oceansoft.apiservice.plugins
 * Created by smc
 * date on 2016/3/2.
 * Email:sunmch@163.com
 * 驾照查询插件
 */
public class DriverLicenseQueryPlugn implements IProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DriverLicenseQueryPlugn.class);

    private String jszjfSql = "select xm,zjcx,ljjf,yxqz,djzsxxdz from trff_cy.v_sj_drivinglicense where dabh=? and sfzmhm=?";
    @Override
    public String command() {
        return "010107";
    }

    @Override
    public RespCmd doQuery(ReqCmd reqCmd) {
        logger.debug("记分查询,线程:[{}],请求对象:{}", Thread.currentThread(), JSON.toJSONString(reqCmd));
        RespCmd respCmd = new RespCmd();
        DriverLicenseSearchCondition driverLicenseSearchCondition = JSON.parseObject(reqCmd.getCommand(), DriverLicenseSearchCondition.class);
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        JDBCPool jdbcPool = null;
        Connection connection = null;
        try {
            //InnerCoreProcessor innerCoreProcessor = new InnerCoreProcessor();
            //jdbcPool = JDBCPool.getInstance("/properties/dcn-config.properties");
            //connection = jdbcPool.open();
            //connection = DBAccess.getInstance().getConnection();
            connection = JjConnection.getConnection();

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
            statement = connection.prepareStatement(jszjfSql);
            statement.setString(1,driverLicenseSearchCondition.getDabh());
            statement.setString(2,driverLicenseSearchCondition.getJtglywdxsfzhm());
            resultSet = statement.executeQuery();

            // 累计记分
            String ljjf = "-1";
            // 姓名
            String xm = "";
            // 准驾车型
            String zjcx = "";
            // 有效期至
            String yxqz = "";
            // 登记地址
            String djzsxxdz = "";
            if (null != resultSet && resultSet.next()) {
                ljjf = resultSet.getString("ljjf");
                xm = resultSet.getString("xm");
                zjcx = resultSet.getString("zjcx");
                yxqz = resultSet.getString("yxqz");
                djzsxxdz = resultSet.getString("djzsxxdz");
            }
            if(!"-1".equals(ljjf)){
//                {"jdcjszljjf":"0","jtglywdxsfzhm":"220523199409152813"}
                DriverLicenseInfo driverLicenseInfo = new DriverLicenseInfo();
                driverLicenseInfo.setJtglywdxsfzhm(driverLicenseSearchCondition.getJtglywdxsfzhm());
                driverLicenseInfo.setJdcjszljjf(ljjf);
                driverLicenseInfo.setXm(xm);
                driverLicenseInfo.setZjcx(zjcx);
                driverLicenseInfo.setDabh(driverLicenseSearchCondition.getDabh());
                driverLicenseInfo.setYxqs(yxqz);
                driverLicenseInfo.setDjzsxxdz(djzsxxdz);
                String resp = JSON.toJSONString(driverLicenseInfo);
                respCmd.setResult(resp);
                respCmd.setSucc(true);
                respCmd.setCode("000");
                respCmd.setMsg("查询成功");
            }else{
                respCmd.setResult(null);
                respCmd.setMsg("您输入的驾驶证号与档案编号不符，请重新输入");
                respCmd.setCode("111111");
                respCmd.setSucc(false);
                return respCmd;
            }
            statement.close();
            resultSet.close();
            logger.info(driverLicenseSearchCondition.getJtglywdxsfzhm()+"，驾驶证记分查询结束");
        }catch(Exception e){
            e.printStackTrace();
            logger.info(driverLicenseSearchCondition.getJtglywdxsfzhm()+"，驾驶证记分查询出错");
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
