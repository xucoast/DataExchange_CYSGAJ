package cn.com.oceansoft.apiservice.plugins;

import cn.com.oceansoft.apiservice.db.DBAccess;
import cn.com.oceansoft.apiservice.db.JjConnection;
import cn.com.oceansoft.apiservice.entity.DriverLicenseInfo;
import cn.com.oceansoft.apiservice.entity.DriverLicenseSearchCondition;
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

/**
 * cn.ucox.web.logservice.modules.dcn.plugin
 * 命令字：010104
 * Created by smc on 2016/2/22.
 * Email:sunmch@163.com
 * 驾驶员信息查询插件
 */
public class DriverQueryPlugin implements IProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DriverQueryPlugin.class);

    private String jszjfSql = "select xm,xb,gj,csrq,sjhm,lxzsxxdz,sfzmmc,gxsj,jxmc,sfzmhm,jbr,fzrq,dzyx,zjcx,ljjf,yxqz,cfrq,cclzrq,ccfzjg,syyxqz,dabh,djzsxxdz from trff_cy.v_sj_drivinglicense where dabh=? and sfzmhm=?";

    @Override
    public String command() {
        return "010104";
    }

    @Override
    public RespCmd doQuery(ReqCmd reqCmd) {
        logger.debug("驾驶员查询,线程:[{}],请求对象:{}", Thread.currentThread(), JSON.toJSONString(reqCmd));
        JDBCPool jdbcPool = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        RespCmd respCmd = new RespCmd();
        DriverLicenseSearchCondition driverLicenseSearchCondition = JSON.parseObject(reqCmd.getCommand(), DriverLicenseSearchCondition.class);
        Connection connection = null;
        try {
            //InnerCoreProcessor innerCoreProcessor = new InnerCoreProcessor();
            //jdbcPool = JDBCPool.getInstance("/properties/dcn-config.properties");
            //connection = jdbcPool.open();
            //connection = DBAccess.getInstance().getConnection();
            connection = JjConnection.getConnection();

            respCmd.setSerialNum(reqCmd.getSerialNum());

            if (StringUtils.isBlank(driverLicenseSearchCondition.getDabh())) {
                respCmd.setSucc(false);
                respCmd.setCode("1");
                respCmd.setMsg("请输入档案编号");
                return respCmd;
            }
            if (StringUtils.isBlank(driverLicenseSearchCondition.getJtglywdxsfzhm())) {
                respCmd.setSucc(false);
                respCmd.setCode("1");
                respCmd.setMsg("请输入驾驶证号");
                return respCmd;
            }
            statement = connection.prepareStatement(jszjfSql);
            statement.setString(1, driverLicenseSearchCondition.getDabh());
            statement.setString(2, driverLicenseSearchCondition.getJtglywdxsfzhm());
            resultSet = statement.executeQuery();

            if (null != resultSet && resultSet.next()) {
                DriverLicenseInfo driverLicenseInfo = new DriverLicenseInfo();
                driverLicenseInfo.setJtglywdxsfzhm(driverLicenseSearchCondition.getJtglywdxsfzhm());
                driverLicenseInfo.setJdcjszljjf(resultSet.getString("ljjf"));
                // 姓名
                driverLicenseInfo.setXm(resultSet.getString("xm"));
                // 性别
                driverLicenseInfo.setXb(resultSet.getString("xb"));
                // 国籍
                driverLicenseInfo.setGj(resultSet.getString("gj"));
                // 手机号码
                driverLicenseInfo.setLxdh(resultSet.getString("sjhm"));
                // 联系住所详细地址
                driverLicenseInfo.setLxzsxxdz(resultSet.getString("lxzsxxdz"));
                // 身份证明名称
                driverLicenseInfo.setSfzmmc(resultSet.getString("sfzmmc"));
                // 更新时间
                driverLicenseInfo.setGxsj(resultSet.getString("gxsj"));
                // 驾校名称
                driverLicenseInfo.setJxmc(resultSet.getString("jxmc"));
                // 身份证明号码
                driverLicenseInfo.setSfzmhm(resultSet.getString("sfzmhm"));
                // 经办人
                driverLicenseInfo.setJbr(resultSet.getString("jbr"));
                // 发证日期
                driverLicenseInfo.setFzrq(resultSet.getString("fzrq"));
                // 电子邮箱
                driverLicenseInfo.setDzyx(resultSet.getString("dzyx"));
                // 出生日期
                driverLicenseInfo.setCsrq(resultSet.getString("csrq"));
                // 超分日期
                driverLicenseInfo.setJdcjszcfrq(resultSet.getString("cfrq"));
                // 初次领证日期
                driverLicenseInfo.setCclzrq(resultSet.getString("cclzrq"));
                // 初次发证机关
                driverLicenseInfo.setCcfzjg(resultSet.getString("ccfzjg"));
                // 审验有效期止
                driverLicenseInfo.setSyyxqz(resultSet.getString("syyxqz"));
                // 准驾车型
                driverLicenseInfo.setZjcx(resultSet.getString("zjcx"));
                // 档案编号
                driverLicenseInfo.setDabh(driverLicenseSearchCondition.getDabh());
                // 有效期至
                driverLicenseInfo.setYxqs(resultSet.getString("yxqz"));
                // 登记住所详细地址
                driverLicenseInfo.setDjzsxxdz(resultSet.getString("djzsxxdz"));
                String resp = JSON.toJSONString(driverLicenseInfo);
                respCmd.setResult(resp);
                respCmd.setSucc(true);
                respCmd.setCode("000");
                respCmd.setMsg("查询成功");
            } else {
                respCmd.setResult(null);
                respCmd.setMsg("您输入的驾驶证号与档案编号不符，请重新输入");
                respCmd.setCode("111111");
                respCmd.setSucc(false);
                return respCmd;
            }
            statement.close();
            resultSet.close();
            logger.info(driverLicenseSearchCondition.getJtglywdxsfzhm() + "，驾驶证信息查询结束");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(driverLicenseSearchCondition.getJtglywdxsfzhm() + "，驾驶证信息查询出错");
        } finally {
            if (statement != null) {
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
