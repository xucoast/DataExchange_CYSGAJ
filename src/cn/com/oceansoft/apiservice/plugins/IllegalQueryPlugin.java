/**
 * cn.ucox.web.logservice.modules.dcn.plugin.IllegalQueryPlugin
 *
 * @author chenw
 * @create 16/2/20.01:06
 * @email javacspring@hotmail.com
 */

package cn.com.oceansoft.apiservice.plugins;

import cn.com.oceansoft.apiservice.dao.QueryDao;
import cn.com.oceansoft.apiservice.entity.OffenseVehicleInfo2;
import cn.com.oceansoft.apiservice.entity.OffenseVehicleSearchCondition;
import cn.com.oceansoft.apiservice.util.RPCClientUtils;
import cn.com.oceansoft.apiservice.util.VehicleQueryUtils;
import cn.ucox.web.framework.rendors.dcn.entity.ReqCmd;
import cn.ucox.web.framework.rendors.dcn.entity.RespCmd;
import cn.ucox.web.framework.rendors.dcn.processor.IProcessor;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆违章查询服务插件业务实现
 * 命令字：
 *此类由于接口申请错误，作废
 * @author chenw
 * @create 16/2/20 01:06
 * @email javacspring@gmail.com
 */
public class IllegalQueryPlugin implements IProcessor {

    private static final Logger logger = LoggerFactory.getLogger(IllegalQueryPlugin.class);

    private static final Logger log = LoggerFactory.getLogger("VehicleServiceImpl");
    private static final String webserviceUrl = "http://zyfw.jl/dsp/services/GabRequestServiceServer?wsdl";
    private static final String targetNamespace = "http://server.webservice.dsp.dm.com";
    private static final String methodName = "QueryToXml";
    private static final String[] EndUser = {"雷春丽", "220182198209070024", "吉林省公安厅科技通信处系统运行科", "220182198209070024"};
    private static final String SenderID = "C00-00000018";   //请求方ID
    private static final String ServiceID = "220000312170304";   ////目标方ID
    private static final String DataObjectCode = "dataset_C56C0A690BD226C030F86E02776D0A3A";  //共享数据项集代码
    private static final String[] RequiredItems = {"CLSJ", "DLLKLDDM", "FKJE", "GDDH", "JBR1", "JBR2", "JDCHPHM", "JDCHPZLDM", "JDCSYRMC", "JKFS", "JKRQ", "JSJG", "LRR", "LRSJ", "LXFS", "NL", "SGDJ", "WFDZ", "WFSJ", "ZJMC", "JTWFXWDM"};    //信息查询条件

    @Override
    public String command() {
        return "222222";
    }

    @Override
    public RespCmd doQuery(ReqCmd reqCmd) {
        logger.debug("违章查询,线程:[{}],请求对象:{}", Thread.currentThread(), JSON.toJSONString(reqCmd));
        RespCmd respCmd = new RespCmd();
        respCmd.setSerialNum(reqCmd.getSerialNum());
        OffenseVehicleSearchCondition condition = JSON.parseObject(reqCmd.getCommand(), OffenseVehicleSearchCondition.class);
        StringBuilder sqlStr = new StringBuilder(" JKRQ IS NULL ");
        if (StringUtils.isNotBlank(condition.getJdccllxbm())) {
            sqlStr.append(" AND JDCCLLXDM = '").append(condition.getJdccllxbm()).append("'");
        }
        //机动车号牌号码
        if (StringUtils.isNotBlank(condition.getJdchphm()) && !"0".equals(condition.getJdchphm())) {
            sqlStr.append("  AND JDCHPHM='").append(condition.getJdchphm()).append("'");
        }
        //信息查询条件
        String Condition = sqlStr.toString();
        logger.debug("查询参数:{}", sqlStr.toString());
        //请求参数
        Object[][] requestString = {{SenderID}, {ServiceID}, EndUser, {DataObjectCode}, {Condition}, RequiredItems, {""}};
        try {
            //接口返回结果
            Object[] responseObject = RPCClientUtils.getInstance().invoke(webserviceUrl, targetNamespace, methodName, requestString);
            if (null != responseObject) {
                String respStr = responseObject[0].toString();
                doExecute(respStr, respCmd, condition.getFdj());
            } else {
                log.error("查询车辆违章信息失败,即将进行第二次查询");
                Thread.sleep(1000L);
                responseObject = RPCClientUtils.getInstance().invoke(webserviceUrl, targetNamespace, methodName, requestString);
                if (null != responseObject) {
                    String respStr = responseObject[0].toString();
                    doExecute(respStr, respCmd, condition.getFdj());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return respCmd;
    }

    private void doExecute(String respStr, RespCmd respCmd, String fdj) throws Exception {
        Document document = DocumentHelper.parseText(respStr);
        Element rootElement = document.getRootElement();
        Element items = rootElement.element("Method").element("Items");
        Element valueEle = items.element("Item").element("Value");
        List<Element> rows = valueEle.elements();
        Element rowCode = rows.get(0);
        List<Element> rowCodeList = rowCode.elements();
        String code = rowCodeList.get(0).getText();
        if (rows.size() > 2) {
            if (VehicleQueryUtils.queryVehicle(fdj)) {
                List<OffenseVehicleInfo2> offenseVehicleInfos = new ArrayList<>(10);
                for (int i = 2; i < rows.size(); i++) {
                    Element rowValue = rows.get(i);
                    OffenseVehicleInfo2 offenseVehicleInfo = getOffenseVehicleInfoByXml(rowValue);
                    offenseVehicleInfos.add(offenseVehicleInfo);
                }
                String resp = JSON.toJSONString(offenseVehicleInfos);
                respCmd.setResult(resp);
                respCmd.setMsg("成功");
                respCmd.setCode(code);
                respCmd.setSucc(true);
            } else {
                respCmd.setCode(code);
                if (!"000".equals(code)) {
                    respCmd.setSucc(false);
                    respCmd.setMsg("查询失败，查看返回码");
                } else {
                    respCmd.setSucc(true);
                    respCmd.setMsg("查询成功");
                }
            }
        } else {
            respCmd.setCode(code);
            if (!"000".equals(code)) {
                respCmd.setSucc(false);
                respCmd.setMsg("查询失败，查看返回码");
            } else {
                respCmd.setSucc(true);
                respCmd.setMsg("查询成功");
            }
        }
    }

    private OffenseVehicleInfo2 getOffenseVehicleInfoByXml(Element rowValue) {
        OffenseVehicleInfo2 offenseVehicleInfo = new OffenseVehicleInfo2();
        List<Element> rowValues = rowValue.elements();
        offenseVehicleInfo.setClsj(rowValues.get(0).getText());
        offenseVehicleInfo.setDllklddm(rowValues.get(1).getText());
        offenseVehicleInfo.setFkje(rowValues.get(2).getText());
        offenseVehicleInfo.setGddh(rowValues.get(3).getText());
        offenseVehicleInfo.setJbr1(rowValues.get(4).getText());
        offenseVehicleInfo.setJbr2(rowValues.get(5).getText());
        offenseVehicleInfo.setJdchphm(rowValues.get(6).getText());
        offenseVehicleInfo.setJdchpzldm(rowValues.get(7).getText());
        offenseVehicleInfo.setJdcsyrmc(rowValues.get(8).getText());
        offenseVehicleInfo.setJkfs(rowValues.get(9).getText());
        offenseVehicleInfo.setJkrq(rowValues.get(10).getText());
        offenseVehicleInfo.setJsjg(rowValues.get(11).getText());
        offenseVehicleInfo.setLrr(rowValues.get(12).getText());
        offenseVehicleInfo.setLrsj(rowValues.get(13).getText());
        offenseVehicleInfo.setLxfs(rowValues.get(14).getText());
        offenseVehicleInfo.setNl(rowValues.get(15).getText());
        offenseVehicleInfo.setSgdj(rowValues.get(16).getText());
        offenseVehicleInfo.setWfdz(rowValues.get(17).getText());
        offenseVehicleInfo.setWfsj(rowValues.get(18).getText());
        offenseVehicleInfo.setZjmc(rowValues.get(19).getText());
        QueryDao queryDao = QueryDao.getInstance("/properties/dcn-config.properties");
        String wfxwdm = rowValues.get(20).getText();
//        offenseVehicleInfo.setWzyy(queryDao.getWfyy(wfxwdm));
        return offenseVehicleInfo;
    }

    @Override
    public void doResponse(RespCmd respCmd) {

    }

}
