package cn.com.oceansoft.apiservice.plugins;

import cn.com.oceansoft.apiservice.db.DBAccess;
import cn.com.oceansoft.apiservice.db.JjConnection;
import cn.com.oceansoft.apiservice.entity.OffenseVehicleSearchCondition;
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
 * cn.ucox.web.logservice.modules.dcn.plugin
 * 命令字：010103
 * Created by smc on 2016/2/22.
 * Email:sunmch@163.com
 * 车辆查询插件
 */
public class VehicleQueryPlugin implements IProcessor {
    private static final Logger logger = LoggerFactory.getLogger(VehicleQueryPlugin.class);
    // 查询车辆基本信息视图 发动机号
    private String vehicleInfoSql = "select hpzl,hphm,fdjh,syr,sjhm,zt,yxqz,sfzmhm from trff_cy.V_SJ_VEHICLE where hphm=? and hpzl=? and fdjh like ?";

    @Override
    public String command() {
        return "010103";
    }

    @Override
    public RespCmd doQuery(ReqCmd reqCmd) {
        logger.debug("车辆查询,线程:[{}],请求对象:{}", Thread.currentThread(), JSON.toJSONString(reqCmd));
        OffenseVehicleSearchCondition contion = JSON.parseObject(reqCmd.getCommand(), OffenseVehicleSearchCondition.class);
        RespCmd respCmd = new RespCmd();
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
            statement = connection.prepareStatement(vehicleInfoSql);
            statement.setString(1,contion.getJdchphm().replace("辽",""));
            statement.setString(2,contion.getJdccllxbm());
            statement.setString(3,"%"+contion.getFdj()+"%");
            resultSet = statement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("hpzl", resultSet.getString("hpzl"));
                jsonObject.put("hphm", resultSet.getString("hphm"));
                jsonObject.put("fdjh", resultSet.getString("fdjh"));
                jsonObject.put("syr", resultSet.getString("syr"));
                jsonObject.put("sjhm", resultSet.getString("sjhm"));
                jsonObject.put("zt", resultSet.getString("zt"));
                jsonObject.put("yxqz", resultSet.getString("yxqz"));
                jsonObject.put("sfzmhm", resultSet.getString("sfzmhm"));
                respCmd.setResult(jsonObject.toString());
                respCmd.setSucc(true);
                respCmd.setCode("000");
                respCmd.setMsg("查询成功");
            } else {
                respCmd.setResult(null);
                respCmd.setMsg("未检索到与此车牌号及发动机号相匹配的车辆，请重新输入");
                respCmd.setCode("111111");
                respCmd.setSucc(false);
                return respCmd;
            }

            statement.close();
            resultSet.close();
            logger.info(contion.getJdchphm()+"，车辆基本信息查询结束");
        }catch(Exception e){
            e.printStackTrace();
            logger.info(contion.getJdchphm()+"，车辆基本信息查询出错");
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
