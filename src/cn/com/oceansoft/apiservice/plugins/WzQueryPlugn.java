package cn.com.oceansoft.apiservice.plugins;

import cn.com.oceansoft.apiservice.db.DBAccess;
import cn.com.oceansoft.apiservice.db.JjConnection;
import cn.com.oceansoft.apiservice.entity.OffenseVehicleInfo;
import cn.com.oceansoft.apiservice.entity.OffenseVehicleSearchCondition;
import cn.ucox.web.framework.rendors.dcn.entity.ReqCmd;
import cn.ucox.web.framework.rendors.dcn.entity.RespCmd;
import cn.ucox.web.framework.rendors.dcn.processor.IProcessor;
import cn.ucox.web.framework.rendors.dcn.processor.InnerCoreProcessor;
import cn.ucox.web.framework.rendors.dcn.utils.JDBCPool;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
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
 * date on 2016/3/17.
 *
 * Email:sunmch@163.com
 * 车辆违章查询
 */
public class WzQueryPlugn implements IProcessor {

    private static final Logger logger = LoggerFactory.getLogger(WzQueryPlugn.class);
    /*// 查询车辆基本信息视图 发动机号
    private String vehicleInfoSql = "select count(*) num from trff_cy.V_SJ_VEHICLE@cyjj where hphm=? and hpzl=? and fdjh like ?";
    // 查询朝阳交警违章视图 cyjj
    private static String wzInfoSql = "select hpzl,hphm,wfdz,wfsj,wfjfs,fkje_dut,wfnr,clbj,cjjg from trff_cy.V_SJ_VIO_SERVEIL@cyjj where clbj='0' and hphm=? and hpzl=?";*/
// 查询车辆基本信息视图 发动机号
    private String vehicleInfoSql = "select count(*) num from trff_cy.V_SJ_VEHICLE where hphm=? and hpzl=? and fdjh like ?";
    // 查询朝阳交警违章视图 cyjj
    private static String wzInfoSql = "select hpzl,hphm,wfdz,wfsj,wfjfs,fkje_dut,wfnr,clbj,cjjg,wfjfs,vehzt,drvzt,zjcx,ljjf,sfzmhm from trff_cy.V_SJ_VIO_SERVEIL where clbj='0' and hphm=? and hpzl=?";
    @Override
    public String command() {
        return "010102";
    }

    @Override
    public RespCmd doQuery(ReqCmd reqCmd) {
        logger.debug("违章查询,线程:[{}],请求对象:{}", Thread.currentThread(), JSON.toJSONString(reqCmd));
        RespCmd respCmd = new RespCmd();
        JDBCPool jdbcPool = null;
        PreparedStatement statement = null;
        PreparedStatement wzxxStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        ResultSet wzResultSet = null;
        OffenseVehicleSearchCondition contion = JSON.parseObject(reqCmd.getCommand(), OffenseVehicleSearchCondition.class);
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

            String num = "0";
            if (null != resultSet && resultSet.next()) {
                num = resultSet.getString(1);
            }
            if(!"0".equals(num)){
                wzxxStatement = connection.prepareStatement(wzInfoSql);
                wzxxStatement.setString(1,contion.getJdchphm());
                wzxxStatement.setString(2,contion.getJdccllxbm());
                wzResultSet = wzxxStatement.executeQuery();

                if(wzResultSet != null){
                    List<OffenseVehicleInfo> offenseVehicleInfos = new ArrayList<OffenseVehicleInfo>();
                    while(wzResultSet.next()){
                        OffenseVehicleInfo offenseVehicleInfo = new OffenseVehicleInfo();
                        offenseVehicleInfo.setJdchphm(contion.getJdchphm());
                        offenseVehicleInfo.setJdchpzldm(contion.getJdccllxbm());
                        // 违法地址
                        offenseVehicleInfo.setWfdd(wzResultSet.getString("wfdz"));
                        // 违法时间
                        offenseVehicleInfo.setWfsj(wzResultSet.getString("wfsj"));
                        // 扣分
                        offenseVehicleInfo.setKf(Integer.parseInt(wzResultSet.getString("wfjfs")));
                        // 罚款金额
                        offenseVehicleInfo.setFkje(wzResultSet.getString("fkje_dut"));
                        // 违法内容
                        offenseVehicleInfo.setWzyy(wzResultSet.getString("wfnr"));
                        // 处理标记
                        offenseVehicleInfo.setClbj(wzResultSet.getString("clbj"));
                        // 采集机构
                        offenseVehicleInfo.setCjjg(wzResultSet.getString("cjjg"));
                        // 车辆状态
                        offenseVehicleInfo.setVehzt(wzResultSet.getString("vehzt"));
                        // 驾驶证状态
                        offenseVehicleInfo.setDrvzt(wzResultSet.getString("drvzt"));
                        // 准驾车型
                        offenseVehicleInfo.setZjcx(wzResultSet.getString("zjcx"));
                        // 累计记分
                        offenseVehicleInfo.setLjjf(wzResultSet.getString("ljjf"));
                        offenseVehicleInfos.add(offenseVehicleInfo);
                    }
                    if(offenseVehicleInfos.size()>0){
                        respCmd.setResult(JSON.toJSONString(offenseVehicleInfos));
                    }else{
                        respCmd.setResult(null);
                    }
                    respCmd.setMsg("成功");
                    respCmd.setCode("000");
                    respCmd.setSucc(true);
                    return respCmd;
                }
            }else{
                respCmd.setResult(null);
                respCmd.setMsg("您输入的车牌号与发动机号不符，请重新输入");
                respCmd.setCode("111111");
                respCmd.setSucc(false);
                return respCmd;
            }
            respCmd.setResult(null);
            respCmd.setSucc(true);
            respCmd.setCode("000");
            respCmd.setMsg("查询成功");

            statement.close();
            wzxxStatement.close();
            resultSet.close();
            logger.info(contion.getJdchphm()+"，违章查询结束");
        }catch(Exception e){
            e.printStackTrace();
            logger.info(contion.getJdchphm()+"，违章查询出错");
        }finally {
            if(wzxxStatement != null){
                try {
                    wzxxStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
