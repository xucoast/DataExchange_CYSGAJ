package cn.com.oceansoft.apiservice.plugins;

import cn.com.oceansoft.apiservice.dao.QueryDao;
import cn.com.oceansoft.apiservice.db.DBAccess;
import cn.com.oceansoft.apiservice.entity.NameSakeInfo;
import cn.com.oceansoft.apiservice.entity.NameSakeSearchCondition;
import cn.com.oceansoft.apiservice.entity.PermanentPopulationSearchCondition;
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
 * Created by xuhb
 * date on 2016-08-05
 * 重名查询
 */
public class NameSakeQueryPlugin implements IProcessor {
    private static final Logger logger = LoggerFactory.getLogger(NameSakeQueryPlugin.class);
    // 查询人口库视图 v_czrk_jksj
    private static String persionInfo = "select count(1) from v_czrk_jksj@cyrk where xm=?";

    @Override
    public String command() {
        return "010106";
    }

    @Override
    public RespCmd doQuery(ReqCmd reqCmd) {
        JDBCPool jdbcPool = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        RespCmd respCmd = new RespCmd();
        NameSakeSearchCondition nameSakeSearchCondition = JSON.parseObject(reqCmd.getCommand(), NameSakeSearchCondition.class);
        try {
            //InnerCoreProcessor innerCoreProcessor = new InnerCoreProcessor();
            //jdbcPool = JDBCPool.getInstance("/properties/dcn-config.properties");
            //connection = jdbcPool.open();
            connection = DBAccess.getInstance().getConnection();

            respCmd.setSerialNum(reqCmd.getSerialNum());

            if(StringUtils.isBlank(nameSakeSearchCondition.getXm())){
                respCmd.setSucc(false);
                respCmd.setCode("1");
                respCmd.setMsg("请输入姓名");
                return respCmd;
            }

            statement = connection.prepareStatement(persionInfo);
            statement.setString(1,nameSakeSearchCondition.getXm());
            resultSet = statement.executeQuery();
            // {"xm":"王勇"}
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("xm",nameSakeSearchCondition.getXm());
            int sameNameNum = 0;
            if (null != resultSet && resultSet.next()) {
                sameNameNum = resultSet.getInt(1);
            }
            NameSakeInfo nameSakeInfo = new NameSakeInfo();
            nameSakeInfo.setCount(sameNameNum);
            nameSakeInfo.setName(nameSakeSearchCondition.getXm());
            // 查询结果 {"count":5339,"name":"王勇"}
            respCmd.setResult(JSON.toJSONString(nameSakeInfo));
            respCmd.setSucc(true);
            respCmd.setCode("000");
            respCmd.setMsg("查询成功");
            statement.close();
            resultSet.close();
            logger.info(nameSakeSearchCondition.getXm()+"，重名查询结束");
        }catch(Exception e){
            e.printStackTrace();
            logger.info(nameSakeSearchCondition.getXm()+"，重名查询出错");
            respCmd.setSucc(false);
            respCmd.setCode("1");
            respCmd.setMsg(e.getMessage());
            return respCmd;
        }finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            //jdbcPool.close(connection);
        }
        return respCmd;
    }

    @Override
    public void doResponse(RespCmd respCmd) {

    }
}
