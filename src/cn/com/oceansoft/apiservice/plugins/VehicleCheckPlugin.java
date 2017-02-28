package cn.com.oceansoft.apiservice.plugins;

import cn.com.oceansoft.apiservice.db.DBAccess;
import cn.com.oceansoft.apiservice.db.JjConnection;
import cn.com.oceansoft.apiservice.entity.OffenseVehicleSearchCondition;
import cn.ucox.web.framework.rendors.dcn.entity.ReqCmd;
import cn.ucox.web.framework.rendors.dcn.entity.RespCmd;
import cn.ucox.web.framework.rendors.dcn.processor.IProcessor;
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
import java.sql.Date;

/**
 * cn.ucox.web.logservice.modules.dcn.plugin
 * 命令字：010109
 * Created by smc on 2016/2/22.
 * Email:sunmch@163.com
 * 车辆查询插件
 */
public class VehicleCheckPlugin implements IProcessor {
    private static final Logger logger = LoggerFactory.getLogger(VehicleCheckPlugin.class);
    // 查询车辆基本信息视图 发动机号
    private String vehicleInfoSql = "select yxqz from trff_cy.V_SJ_VEHICLE where sfzmhm=? and hphm=? and hpzl=? and fdjh like ?";

    @Override
    public String command() {
        return "010109";
    }

    @Override
    public RespCmd doQuery(ReqCmd reqCmd) {
        logger.debug("车辆校验,线程:[{}],请求对象:{}", Thread.currentThread(), JSON.toJSONString(reqCmd));
        OffenseVehicleSearchCondition contion = JSON.parseObject(reqCmd.getCommand(), OffenseVehicleSearchCondition.class);
        RespCmd respCmd = new RespCmd();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        JDBCPool jdbcPool = null;
        Connection connection = null;
        Connection connectionJj = null;
        try {
            //InnerCoreProcessor innerCoreProcessor = new InnerCoreProcessor();
            //jdbcPool = JDBCPool.getInstance("/properties/dcn-config.properties");
            //connection = jdbcPool.open();


            respCmd.setSerialNum(reqCmd.getSerialNum());

            if(StringUtils.isBlank(contion.getFdj())){
                respCmd.setSucc(false);
                respCmd.setCode("1");
                respCmd.setMsg("请输入发动机号");
                return respCmd;
            }
            if(StringUtils.isBlank(contion.getJdchphm())){
                respCmd.setSucc(false);
                respCmd.setCode("1");
                respCmd.setMsg("请输入车牌号");
                return respCmd;
            }
            if(StringUtils.isBlank(contion.getJdccllxbm())){
                respCmd.setSucc(false);
                respCmd.setCode("1");
                respCmd.setMsg("请输入车辆类型");
                return respCmd;
            }
            if(StringUtils.isBlank(contion.getSfzh())){
                respCmd.setSucc(false);
                respCmd.setCode("1");
                respCmd.setMsg("请输入身份证号码");
                return respCmd;
            }
            if(StringUtils.isBlank(contion.getMemcarid())){
                respCmd.setSucc(false);
                respCmd.setCode("1");
                respCmd.setMsg("请输入memcarid");
                return respCmd;
            }

            connectionJj = JjConnection.getConnection();
            statement = connectionJj.prepareStatement(vehicleInfoSql);
            statement.setString(1,contion.getSfzh());
            statement.setString(2,contion.getJdchphm().replace("辽",""));
            statement.setString(3,contion.getJdccllxbm());
            statement.setString(4,"%"+contion.getFdj()+"%");
            resultSet = statement.executeQuery();
            Date yxqz = null;
            if (resultSet != null && resultSet.next()) {
                yxqz = resultSet.getDate("yxqz");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("yxqz", yxqz);

                respCmd.setResult(jsonObject.toString());
                respCmd.setSucc(true);
                respCmd.setCode("000");
                respCmd.setMsg("查询成功");
            }
            statement = null;
            logger.info(contion.getJdchphm()+"，车辆校验查询交警视图结束"+yxqz);
            ResultSet rs = DBAccess.getInstance().doQuery("select guid from PASC_MEMBERS_CAR_OUT where guid='"+contion.getMemcarid()+"'");
            connection = DBAccess.getInstance().getConnection();
            if(rs!=null && rs.next()){
                String updateSql = "update PASC_MEMBERS_CAR_OUT set identityStatus=?,duedate=? where guid=?";
                statement = connection.prepareStatement(updateSql);
                statement.setString(1,yxqz==null?"-9":"1");
                statement.setDate(2,yxqz);
                statement.setString(3,contion.getMemcarid());
                statement.execute();
                connection.commit();
                statement.close();
                resultSet.close();
            }else{
                String sql = "insert into PASC_MEMBERS_CAR_OUT(guid,identityStatus,msg,duedate) values(?,?,?,?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, contion.getMemcarid());
                statement.setString(2, yxqz==null?"-9":"1");
                statement.setString(3, "成功");
                statement.setDate(4,yxqz);
                statement.execute();
                connection.commit();
                statement.close();
                resultSet.close();
            }

            /*connection.commit();
            QueryDao queryDao = new QueryDao();
            queryDao.updateMemberVehicle(num,contion.getUserguid(),connection);*/
            logger.info(contion.getJdchphm()+"，车辆校验结束");
        }catch(Exception e){
            e.printStackTrace();
            logger.info(contion.getJdchphm()+"，车辆校验出错");
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
                if(connectionJj!=null && !connectionJj.isClosed()) {
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
