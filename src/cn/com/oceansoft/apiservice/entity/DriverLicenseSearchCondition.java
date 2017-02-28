package cn.com.oceansoft.apiservice.entity;

/**
 * cn.com.oceansoft.apiservice.entity
 * Created by smc
 * date on 2016/3/2.
 * Email:sunmch@163.com
 * 驾照查询条件信息实体类
 */
public class DriverLicenseSearchCondition {

    private  String dabh;   //   机动车档案编号

//    private String xm;  //姓名

    private String jtglywdxsfzhm;  //JTGLYWDXSFZMHM 交通管理业务对象身份证明号码

    private String userguid;

    public String getJtglywdxsfzhm() {
        return jtglywdxsfzhm;
    }

    public void setJtglywdxsfzhm(String jtglywdxsfzhm) {
        this.jtglywdxsfzhm = jtglywdxsfzhm;
    }

    public String getDabh() {
        return dabh;
    }

    public void setDabh(String dabh) {
        this.dabh = dabh;
    }

    public String getUserguid() {
        return userguid;
    }

    public void setUserguid(String userguid) {
        this.userguid = userguid;
    }

    //    public String getXm() {
//        return xm;
//    }
//
//    public void setXm(String xm) {
//        this.xm = xm;
//    }
}
