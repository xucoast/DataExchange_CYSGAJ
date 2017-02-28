package cn.com.oceansoft.apiservice.entity;

/**
 * cn.ucox.web.logservice.modules.dcn.entity
 * Created by smc on 2016/2/22.
 * Email:sunmch@163.com
 * 发送短信信息实体类
 */
public class SmsSendInfo {

    private  String phoneNumber;
    private  String sendContent;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }
}
