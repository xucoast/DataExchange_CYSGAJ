package cn.com.oceansoft.apiservice.entity;

/**
 * Created by smc on 2016/2/19.
 * 实名认证实体类
 */
public class RealNameAuthInfo {

    private String exist;   //是否存在该用户  Y 存在  N 不存在

    private String xm;

    private String sfzh;

    private  String  sjgsdwmc;    //SJGSDWMC 数据归属单位名称 VARCHAR2

    private String   sspcs;  //SSPCS 所属派出所 VARCHAR2

    public String getExist() {
        return exist;
    }

    public void setExist(String exist) {
        this.exist = exist;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }


    public String getSjgsdwmc() {
        return sjgsdwmc;
    }

    public void setSjgsdwmc(String sjgsdwmc) {
        this.sjgsdwmc = sjgsdwmc;
    }

    public String getSspcs() {
        return sspcs;
    }

    public void setSspcs(String sspcs) {
        this.sspcs = sspcs;
    }


}
