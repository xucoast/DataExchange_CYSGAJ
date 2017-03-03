/**
 * cn.ucox.web.logservice.modules.dcn.plugin.CivilNameQueryPlugin
 *
 * @author xuhb
 * @create 2016-08-05
 * @email javacspring@hotmail.com
 */

package cn.com.oceansoft.apiservice.plugins;

import cn.com.oceansoft.apiservice.dao.QueryDao;
import cn.com.oceansoft.apiservice.db.DBAccess;
import cn.com.oceansoft.apiservice.entity.PermanentPopulationSearchCondition;
import cn.com.oceansoft.apiservice.entity.RealNameAuthInfo;
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
 * @create 2016-08-05
 * 命令字：010101
 * 实名认证查询
 * select gmsfhm,xm,xb,csrq,zzxz,pcsjgdm,pcsjgmc from v_czrk_jksj@cyrk where gmsfhm=? and xm=?
 */
public class CivilNameQueryPlugin implements IProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CivilNameQueryPlugin.class);
    // 查询人口库视图 v_czrk_jksj
    private static String persionInfo = "select pcsjgdm,pcsjgmc from v_czrk_jksj@cyrk where gmsfhm=? and xm=?";

    @Override
    public String command() {
        return "010101";
    }

    @Override
    public RespCmd doQuery(ReqCmd reqCmd) {

        //JDBCPool jdbcPool = null;
        Connection connection = null;
        RespCmd respCmd = new RespCmd();
        PermanentPopulationSearchCondition permanentPopulationSearchCondition = JSON.parseObject(reqCmd.getCommand(), PermanentPopulationSearchCondition.class);
        //System.out.println(permanentPopulationSearchCondition.getXm());
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            //InnerCoreProcessor innerCoreProcessor = new InnerCoreProcessor();
            /*jdbcPool = JDBCPool.getInstance("/properties/dcn-config.properties");
            connection = jdbcPool.open();*/
            connection = DBAccess.getInstance().getConnection();

            respCmd.setSerialNum(reqCmd.getSerialNum());

            if(StringUtils.isBlank(permanentPopulationSearchCondition.getGmsfhm())){
                respCmd.setSucc(false);
                respCmd.setCode("1");
                respCmd.setMsg("身份证号为空");
                return respCmd;
            }

            statement = connection.prepareStatement(persionInfo);
            statement.setString(1,permanentPopulationSearchCondition.getGmsfhm());
            statement.setString(2,permanentPopulationSearchCondition.getXm());
            resultSet = statement.executeQuery();
            /*String sql = "select pcsjgdm,pcsjgmc from v_czrk_jksj@cyrk where gmsfhm='"
                    +permanentPopulationSearchCondition.getGmsfhm()+"' and xm='"
                    +permanentPopulationSearchCondition.getXm()+"'";
            resultSet = DBAccess.getInstance().doQuery(sql.toString());*/

            // 户籍地址 {"exist":"Y","sfzh":"220522196807013317","sjgsdwmc":"团结边防派出所","sspcs":"220582701","xm":"王希山"}
            // {"exist":"N","sfzh":"220523196708164415","xm":"赵颖俊"}
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sfzh",permanentPopulationSearchCondition.getGmsfhm());
            jsonObject.put("xm",permanentPopulationSearchCondition.getXm());
            // 户籍机构代码
            String pcsjgdm = "";
            // 户籍机构名称
            String pcsjgmc = "";
            if (null != resultSet && resultSet.next()) {
                pcsjgdm = resultSet.getString(1);
                pcsjgmc = resultSet.getString(2);
            }
            //System.out.println(pcsjgmc+"--");
            if("".equals(pcsjgdm)){
                jsonObject.put("exist","N");
            }else{
                jsonObject.put("exist","Y");
                jsonObject.put("sjgsdwmc",pcsjgmc);
                jsonObject.put("sspcs",pcsjgdm);
            }
            respCmd.setResult(jsonObject.toString());
            respCmd.setSucc(true);
            respCmd.setCode("000");
            respCmd.setMsg("查询成功");
            resultSet = null;
            /*QueryDao queryDao = new QueryDao();
            queryDao.updateMember(respCmd,permanentPopulationSearchCondition.getUserguid(),connection);*/
            //
            try {
                connection.setAutoCommit(false);
                RealNameAuthInfo realNameAuthInfo = JSON.parseObject(respCmd.getResult(), RealNameAuthInfo.class);
                String pasc_members_mange = "select guid from  pasc_members_mange where guid = ?";
                PreparedStatement pstmt = connection.prepareStatement(pasc_members_mange);
                pstmt.setString(1, permanentPopulationSearchCondition.getUserguid());
                resultSet = pstmt.executeQuery();
                if (resultSet != null && resultSet.next()) {
                    String deleteSql = "delete from pasc_members_mange where guid = ? ";
                    pstmt = connection.prepareStatement(deleteSql);
                    pstmt.setString(1, permanentPopulationSearchCondition.getUserguid());
                    pstmt.execute();
                }
                //System.out.println("222");
                String insertSql = "insert into pasc_members_mange(guid,IDENTITYSTATUS,ISENABLED,ISCHECK,Temp1,Temp2) values(?,?,?,?,?,?)";
                if ("Y".equals(realNameAuthInfo.getExist())) {
                    pstmt = connection.prepareStatement(insertSql);
                    pstmt.setString(1,permanentPopulationSearchCondition.getUserguid());
                    pstmt.setInt(2, 1);
                    pstmt.setInt(3,0);
                    pstmt.setInt(4,0);
                    pstmt.setString(5, realNameAuthInfo.getSspcs());
                    pstmt.setString(6, realNameAuthInfo.getSjgsdwmc());
                } else {
                    pstmt = connection.prepareStatement(insertSql);
                    pstmt.setString(1,permanentPopulationSearchCondition.getUserguid());
                    pstmt.setInt(2, -9);
                    pstmt.setInt(3,0);
                    pstmt.setInt(4,0);
                    pstmt.setString(5, realNameAuthInfo.getSspcs());
                    pstmt.setString(6, realNameAuthInfo.getSjgsdwmc());
                }
                pstmt.execute();
                connection.commit();
                pstmt.close();
                statement.close();
                resultSet.close();
                //System.out.println("333");
            } catch (Exception ex) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            logger.info(permanentPopulationSearchCondition.getXm()+"，实名认证结束");
        }catch(Exception e){
            e.printStackTrace();
            logger.info(permanentPopulationSearchCondition.getXm()+"，实名认证出错");
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
