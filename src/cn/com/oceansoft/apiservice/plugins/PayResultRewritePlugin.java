package cn.com.oceansoft.apiservice.plugins;

import cn.com.oceansoft.apiservice.util.RPCClientUtils;
import cn.ucox.web.framework.rendors.dcn.entity.ReqCmd;
import cn.ucox.web.framework.rendors.dcn.entity.RespCmd;
import cn.ucox.web.framework.rendors.dcn.processor.IProcessor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.Date;

/**
 * 户政支付结果callback、
 * <p>
 * Created by CHENW on 2016/3/18.
 */
public class PayResultRewritePlugin implements IProcessor {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String webserviceUrl = "http://10.106.28.163:8080/idcardpayweb/services/IDCARD_CHARGE";
    private final String targetNamespace = "http://www.cchongda.com.cn/Idcard_Charge";
    private final String methodName = "ChargeInterface";
    private final String reqStr = "<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><SLH>2201022</SLH><ZFJYH>12345678900</ZFJYH><ZFJE>0.01</ZFJE><ZFQR>1</ZFQR><ZFQRSJ>20160318154103</ZFQRSJ></DATA>]]>";


    @Override
    public String command() {
        return "010108";
    }

    @Override
    public RespCmd doQuery(ReqCmd reqCmd) {
        JSONObject data = JSON.parseObject(reqCmd.getCommand());
        String buildXml = buildXml(data);
        Object[] response = RPCClientUtils.getInstance().invoke(webserviceUrl, targetNamespace, methodName, new Object[]{"",  buildXml});
        logger.debug("\n户政支付结果原始数据：\n{}", buildXml);
        RespCmd cmd = new RespCmd();
        cmd.setCode("000");
        if (null != response && response.length > 0 && null != response[0]) {
            logger.debug("\n户政支付回写响应数据：\n{}", response[0]);
            SAXReader reader = new SAXReader();
            try {
                Document document = reader.read(new ByteArrayInputStream(response[0].toString().getBytes("UTF-8")));
                Element root = document.getRootElement();
                String code = root.element(new QName("CODE")).getTextTrim();
                cmd.setSucc(code.equalsIgnoreCase("00"));
                cmd.setResult(root.element(new QName("MSG")).getTextTrim());
                cmd.setCode(code);
                cmd.setMsg("支付结果回写户政系统" + (code.equalsIgnoreCase("00") ? "成功!" : "失败!"));
            } catch (Exception e) {
                cmd.setMsg("支付结果回写户政系统错误!");
                logger.error("支付结果回写户政系统错误：", e);
            }
        } else {
            cmd.setSucc(false);
            cmd.setMsg("支付结果回写户政系统失败，接口数据无返回");
            logger.debug("支付结果回写户政系统失败，接口数据无返回");
        }
        cmd.setRespTime(new Date());
        return cmd;
    }

    private String buildXml(JSONObject data) {
        String xmlTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><SLH>%s</SLH><ZFJYH>%s</ZFJYH><ZFJE>%s</ZFJE><ZFQR>1</ZFQR><ZFQRSJ>%s</ZFQRSJ></DATA>";
        String slh = data.getString("bizNo");
        String zfjyh = data.getString("settleNo");
        String zfje = data.getString("amt");
        String zfqrsj = data.getString("settleDate");
        return String.format(xmlTemplate, slh, zfjyh, zfje, zfqrsj);
    }

    @Override
    public void doResponse(RespCmd respCmd) {

    }
}
