package cn.com.oceansoft.apiservice.listener;


import cn.ucox.web.framework.rendors.dcn.DataChangeNotificationListener;
import cn.ucox.web.framework.rendors.dcn.processor.InnerCoreProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * cn.com.oceansoft.apiservice.listener
 * Created by smc
 * date on 2016/2/23.
 * Email:sunmch@163.com
 */
public class InitListenerDcn implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(InitListenerDcn.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("开始启动Oracle数据库监听...");
        DataChangeNotificationListener.getInstance().listening(new InnerCoreProcessor("/properties/dcn-config.properties"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("取消oracle数据监听........");
        DataChangeNotificationListener.getInstance().stopListening();
    }
}
