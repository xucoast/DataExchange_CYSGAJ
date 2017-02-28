package cn.com.oceansoft.apiservice.util;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;

/**
 * Created by Administrator on 2016/2/23.
 */
public class RPCClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(RPCClientUtils.class);
    private  static RPCClientUtils instance = null;

//    private static RPCServiceClient serviceClient;

    private RPCClientUtils() {

    }

    public static  RPCClientUtils getInstance(){
        synchronized (RPCClientUtils.class){
            if(instance == null){
                instance = new RPCClientUtils();
            }

        }
        return instance;
    }

    public Object[] invoke(String webServiceUrl, String namespace, String methodName, Object[] parameter) {
        RPCServiceClient serviceClient = null;
        try {
            if(serviceClient == null){
                serviceClient = new RPCServiceClient();
            }
        } catch (AxisFault var2) {
            logger.error("初始化RPCServiceClient异常,异常:", var2.getMessage());
            var2.printStackTrace();
        }
        if(StringUtils.isBlank(webServiceUrl)) {
            logger.error("调用服务[{}]参数错误,缺失WebServiceUrl:", webServiceUrl);
            throw new RuntimeException("缺失WebServiceUrl!");
        } else if(StringUtils.isBlank(namespace)) {
            logger.error("调用服务[{}]参数错误,缺失WebServiceUrl:", namespace);
            throw new RuntimeException("缺失Namespace参数!");
        } else if(StringUtils.isBlank(methodName)) {
            logger.error("调用服务[{}]参数错误,缺失MethodName", methodName);
            throw new RuntimeException("缺失methodName参数!");
        } else {
            Options options = serviceClient.getOptions();
            options.setTimeOutInMilliSeconds(600000L);
//            options.setProperty(HTTPConstants.SO_TIMEOUT,600000L);
//            options.getTimeOutInMilliSeconds()
            EndpointReference targetEPR = new EndpointReference(webServiceUrl);
            options.setTo(targetEPR);
            Class<?>[] classes = new Class[]{String.class};
            QName opAddEntry = new QName(namespace, methodName);
            serviceClient.setOptions(options);
            try {
                return serviceClient.invokeBlocking(opAddEntry, parameter, classes);

            } catch (AxisFault var10) {
                logger.error("调用服务[{}]异常,异常:", webServiceUrl, var10.getMessage());
                return null;
            }
        }
    }
}
