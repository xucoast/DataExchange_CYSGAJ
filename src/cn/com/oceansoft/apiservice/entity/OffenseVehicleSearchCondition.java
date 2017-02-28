package cn.com.oceansoft.apiservice.entity;

/**
 * 违章车辆查询条件
 * Created by smc on 2016/2/19.
 */
public class OffenseVehicleSearchCondition {

   private String  jdccllxbm ;    //JDCCLLXDM 机动车车辆类型代码 CHAR

    private String fdj;  //发动机后六位

    private  String  jdchphm;  //JDCHPHM 机动车号牌号码 VARCHAR2

    private String sjhm;

    private String userguid;

    private String memcarid;

    private String sfzh;

    public String getMemcarid() {
        return memcarid;
    }

    public void setMemcarid(String memcarid) {
        this.memcarid = memcarid;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getJdccllxbm() {
        return jdccllxbm;
    }

    public void setJdccllxbm(String jdccllxbm) {
        this.jdccllxbm = jdccllxbm;
    }

    public String getFdj() {
        return fdj;
    }

    public void setFdj(String fdj) {
        this.fdj = fdj;
    }

    public String getJdchphm() {
        return jdchphm;
    }

    public void setJdchphm(String jdchphm) {
        this.jdchphm = jdchphm;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getUserguid() {
        return userguid;
    }

    public void setUserguid(String userguid) {
        this.userguid = userguid;
    }
}
