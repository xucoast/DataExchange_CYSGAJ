package cn.com.oceansoft.apiservice.plugins;

import cn.com.oceansoft.apiservice.entity.YjZyPersonInfo;
import cn.com.oceansoft.apiservice.entity.YjZyPersonSearchCondition;
import cn.com.oceansoft.apiservice.util.RPCClientUtils;
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
 * cn.ucox.web.logservice.modules.dcn.plugin
 * 命令字：010105
 * Created by smc on 2016/2/22.
 * Email:sunmch@163.com
 * 已决在押人员查询插件
 */
public class YjZyPersonQueryPlugin implements IProcessor {

    private static final Logger log = LoggerFactory.getLogger(YjZyPersonQueryPlugin.class);
    private static final String webserviceUrl = "http://zyfw.jl/dsp/services/GabRequestServiceServer?wsdl";
    private static final String targetNamespace = "http://server.webservice.dsp.dm.com";
    private static final String methodName = "QueryToXml";
    private static final String[] EndUser = {"雷春丽", "220182198209070024", "吉林省公安厅科技通信处系统运行科", "220182198209070024"};
    private static final String SenderID = "C00-00000018";   //请求方ID
    private static final String ServiceID = "220000312130101";   ////目标方ID
    private static final String DataObjectCode = "dataset_F157F1A54C09E44ED39C0AD84BD9AD9D";  //共享数据项集代码
    private static final String[] RequiredItems = {"AY", "KSSMC", "RSRQ", "RSXZ", "SF", "XM", "ZY", "MZMC", "JYRQ", "JSH", "KSSBH", "CSZT", "CSRQ", "CJSJ", "ZJHM", "CYZJLX", "FWCS", "HJDMC", "HYZKDM", "WHCD", "XZZXQ", "YQTXQX"};    //信息查询条件

    @Override
    public String command() {
        return "010105";
    }

    @Override
    public RespCmd doQuery(ReqCmd reqCmd) {
        RespCmd respCmd = new RespCmd();
        respCmd.setSerialNum(reqCmd.getSerialNum());
        YjZyPersonSearchCondition yjZyPersonSearchCondition = JSON.parseObject(reqCmd.getCommand(), YjZyPersonSearchCondition.class);
        StringBuffer stringBuffer = new StringBuffer();
        /**
         * 所有条件 当条件类型包含0 则表示该条件无用
         */
        if (StringUtils.isNotBlank(yjZyPersonSearchCondition.getXm()) && !"0".equals(yjZyPersonSearchCondition.getXm())) {   //查询姓名
            stringBuffer.append("XM='").append(yjZyPersonSearchCondition.getXm()).append("'");
        }
        if (StringUtils.isNotBlank(yjZyPersonSearchCondition.getKssmc()) && !"0".equals(yjZyPersonSearchCondition.getKssmc())) {   //查询看守所名称
            if (stringBuffer.length() > 0) {
                stringBuffer.append(" and KSSMC LIKE '%").append(yjZyPersonSearchCondition.getKssmc()).append("%'");
            } else {
                stringBuffer.append("KSSMC LIKE '%").append(yjZyPersonSearchCondition.getKssmc()).append("%'");
            }
        }
        //信息查询条件
        String Condition = stringBuffer.toString();
        //请求参数
        String[][] requestString = {{SenderID}, {ServiceID}, EndUser, {DataObjectCode}, {Condition}, RequiredItems, {""}};
        try {
            Object[] responseObject = RPCClientUtils.getInstance().invoke(webserviceUrl, targetNamespace, methodName, requestString);
            if (null != responseObject) {
                String respStr = responseObject[0].toString();
                doExecute(respStr,respCmd);
            } else {
                log.error("查询已决在押人员信息失败,即将第二次查询操作");
                Thread.sleep(1000L);
                responseObject = RPCClientUtils.getInstance().invoke(webserviceUrl, targetNamespace, methodName, requestString);
                if (null != responseObject) {
                    String respStr = responseObject[0].toString();
                    doExecute(respStr,respCmd);
                }else{
                    respCmd.setCode("-1");
                    respCmd.setSucc(false);
                    respCmd.setMsg("查询失败，查看返回码");
                }
            }
        } catch (Exception ex) {
            log.error("查询已决在押人员失败,失败信息： ",ex);
        }
        return respCmd;
    }
    private void doExecute(String respStr,RespCmd respCmd) throws  Exception{
        if (StringUtils.isNotBlank(respStr)) {
            Document document = DocumentHelper.parseText(respStr);
            Element rootElement = document.getRootElement();
            Element items = rootElement.element("Method").element("Items");
            Element valueEle = items.element("Item").element("Value");
            List<Element> rows = valueEle.elements();
            Element rowCode = rows.get(0);
            List<Element> rowCodeList = rowCode.elements();
            String code = rowCodeList.get(0).getText();
            if (rows.size() > 2) {
                respCmd.setMsg("返回成功");
                respCmd.setCode(code);
                List<YjZyPersonInfo> yjZyPersonInfos = new ArrayList<>(10);
                for (int i = 2; i < rows.size(); i++) {
                    Element rowValue = rows.get(i);
                    YjZyPersonInfo yjZyPersonInfo = getYjZyPersonInfoByXml(rowValue);
                    yjZyPersonInfos.add(yjZyPersonInfo);
                }
                String resp = JSON.toJSONString(yjZyPersonInfos);
                respCmd.setSucc(true);
                respCmd.setResult(resp);
            } else {
                respCmd.setCode(code);
                if (!"000".equals(code)) {
                    respCmd.setSucc(false);
                    respCmd.setMsg("查询失败，查看返回码");
                } else {
                    respCmd.setSucc(true);
                    respCmd.setMsg("查询成功,返回结果为空");
                }
            }
        }
    }
    private YjZyPersonInfo getYjZyPersonInfoByXml(Element rowValue) {
        YjZyPersonInfo yjZyPersonInfo = new YjZyPersonInfo();
        List<Element> rowValues = rowValue.elements();
        yjZyPersonInfo.setAy(rowValues.get(0).getText());
        yjZyPersonInfo.setKssmc(rowValues.get(1).getText());
        yjZyPersonInfo.setRsrq(rowValues.get(2).getText());
        yjZyPersonInfo.setRsxz(rowValues.get(3).getText());
        yjZyPersonInfo.setSf(rowValues.get(4).getText());
        yjZyPersonInfo.setXm(rowValues.get(5).getText());
        yjZyPersonInfo.setZy(rowValues.get(6).getText());
        yjZyPersonInfo.setMzmc(rowValues.get(7).getText());
        yjZyPersonInfo.setJyrq(rowValues.get(8).getText());
        yjZyPersonInfo.setJsh(rowValues.get(9).getText());
        yjZyPersonInfo.setKssbh(rowValues.get(10).getText());
        yjZyPersonInfo.setCszt(rowValues.get(11).getText());
        yjZyPersonInfo.setCsrq(rowValues.get(12).getText());
        yjZyPersonInfo.setCjsj(rowValues.get(13).getText());
        yjZyPersonInfo.setZjhm(rowValues.get(14).getText());
        yjZyPersonInfo.setCyzjlx(rowValues.get(15).getText());
        yjZyPersonInfo.setFwcs(rowValues.get(16).getText());
        yjZyPersonInfo.setHjdmc(rowValues.get(17).getText());
        yjZyPersonInfo.setHyzkdm(rowValues.get(18).getText());
        yjZyPersonInfo.setWhcd(rowValues.get(19).getText());
        yjZyPersonInfo.setXzzxq(rowValues.get(20).getText());
        yjZyPersonInfo.setYqtxqx(rowValues.get(21).getText());
        return yjZyPersonInfo;
    }

    @Override
    public void doResponse(RespCmd respCmd) {

    }
}
