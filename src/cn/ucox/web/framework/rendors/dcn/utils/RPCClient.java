/**
 * cn.ucox.web.logservice.rendors.orcldcn.utils.RPCClient
 *
 * @author chenw
 * @create 16/2/20.15:08
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.utils;

//import org.apache.axis2.AxisFault;
//import org.apache.axis2.addressing.EndpointReference;
//import org.apache.axis2.client.Options;
//import org.apache.axis2.rpc.client.RPCServiceClient;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.xml.namespace.QName;

/**
 * WebService调用客户端
 * <br>为减少依赖本组件将移除
 *
 * @author chenw
 * @create 16/2/20 15:08
 * @email javacspring@gmail.com
 */
@Deprecated
class RPCClient {

//    private static final Logger logger = LoggerFactory.getLogger(RPCClient.class);
//    public static final RPCClient INSTANCE = new RPCClient();
//    private static RPCServiceClient SERVICE_CLIENT;
//
//    private RPCClient() {
//        try {
//            SERVICE_CLIENT = new RPCServiceClient();
//        } catch (AxisFault axisFault) {
//            logger.error("初始化RPCServiceClient异常,异常:", axisFault.getMessage());
//            axisFault.printStackTrace();
//        }
//    }
//
//    public static RPCClient getInstance() {
//        return INSTANCE;
//    }
//
//    public static void setOption(Options option) {
//
//    }
//
//    public Object[] invoke(String webServiceUrl, String namespace, String methodName, Object[] parameter) {
//        if (StringUtils.isBlank(webServiceUrl)) {
//            logger.error("调用服务[{}]参数错误,缺失WebServiceUrl:", webServiceUrl);
//            throw new RuntimeException("缺失WebServiceUrl!");
//        }
//        if (StringUtils.isBlank(namespace)) {
//            logger.error("调用服务[{}]参数错误,缺失WebServiceUrl:", namespace);
//            throw new RuntimeException("缺失Namespace参数!");
//        }
//        if (StringUtils.isBlank(methodName)) {
//            logger.error("调用服务[{}]参数错误,缺失MethodName", methodName);
//            throw new RuntimeException("缺失methodName参数!");
//        }
//        Options options = SERVICE_CLIENT.getOptions();
//        options.setTimeOutInMilliSeconds(10000);
//        // 指定调用WebService的URL
//        EndpointReference targetEPR = new EndpointReference(webServiceUrl);
//        options.setTo(targetEPR);
//        // 指定webservice方法返回值的数据类型的Class对象
//        Class[] classes = new Class[]{Object.class};
//        // 指定要调用的webservice方法及WSDL文件的命名空间
//        QName opAddEntry = new QName(namespace, methodName);
//        try {
//            return SERVICE_CLIENT.invokeBlocking(opAddEntry, parameter, classes);
//        } catch (AxisFault ex) {
//            logger.error("调用服务[{}]异常,异常:", webServiceUrl, ex.getMessage());
//            return null;
//        }
//    }
}
