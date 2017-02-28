package cn.com.oceansoft.apiservice.util;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * cn.com.oceansoft.apiservice.util
 * Created by smc
 * date on 2016/3/12.
 * Email:sunmch@163.com
 */
public class VehicleQueryUtils {
    private static final Logger log = LoggerFactory.getLogger(VehicleQueryUtils.class);
    private static final String webserviceUrl = "http://zyfw.jl/dsp/services/GabRequestServiceServer?wsdl";
    private static final String targetNamespace = "http://server.webservice.dsp.dm.com";
    private static final String methodName = "QueryToXml";
    private static final String[] EndUser = {"雷春丽", "220182198209070024", "吉林省公安厅科技通信处系统运行科", "220182198209070024"};
    private static final String SenderID = "C00-00000018";   //请求方ID
    private static final String ServiceID = "220000312170701";   ////目标方ID
    private static final String DataObjectCode = "dataset_1BC1467E9F1A60B0617A3FA91276FBC2";  //共享数据项集代码
    private static final String[] RequiredItems = {"BPCS"};    //信息查询条件

    /**
     * 根据发动机号查询数据
     *
     * @param fdj
     */
    public static boolean queryVehicle(String fdj) {
        boolean bool = false;
        StringBuilder sb = new StringBuilder();
        sb.append("FDJH like '%").append(fdj).append("'");
        String[][] requestString = {{SenderID}, {ServiceID}, EndUser, {DataObjectCode}, {sb.toString()}, RequiredItems, {""}};
        try {
            Object[] responseObject = RPCClientUtils.getInstance().invoke(webserviceUrl, targetNamespace, methodName, requestString);
            if (null != responseObject) {
                String respStr = responseObject[0].toString();
                bool = validateFdjh(respStr);
            }else{
                responseObject = RPCClientUtils.getInstance().invoke(webserviceUrl, targetNamespace, methodName, requestString);
                if (null != responseObject) {
                    String respStr = responseObject[0].toString();
                    bool = validateFdjh(respStr);
                }
            }
        } catch (Exception ex) {
            log.error("验证发动机号牌失败,错误信息: ",ex);
        }
        return bool;
    }

    private static boolean validateFdjh(String respStr) throws DocumentException {
        boolean bool = false;
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
                bool = true;
            }
        }
        return bool;
    }
}
