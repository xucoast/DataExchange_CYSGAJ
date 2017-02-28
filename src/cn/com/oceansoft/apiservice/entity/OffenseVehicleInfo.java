package cn.com.oceansoft.apiservice.entity;

/**
 * cn.com.oceansoft.apiservice.entity
 * Created by smc
 * date on 2016/3/17.
 * Email:sunmch@163.com
 * 获取车辆违法查询
 */
public class OffenseVehicleInfo {

    private String  jdcsyrmc;  //JDCSYRMC 机动车所有人名称  VARCHAR2（250） 是

    private String  jdchphm; //JDCHPHM 机动车号牌号码  VARCHAR2（15） 是

    private String  fkje; //FKJE 罚款金额  NUMBER（6） 否

    private String  dsrmc; //DSRMC 当事人名称  VARCHAR2（30） 否

    private String  clppzw; //CLPPZW 车辆品牌(中文)  VARCHAR2（32） 否

    private String  jtwfxwdm;  //JTWFXWDM 交通违法行为代码  VARCHAR2（5） 否

    private String  cldxbj;   //CLDXBJ 处理对象标记（0-本地;1-本省外地市;2-外省）  CHAR（1） 否

    private String   zqmj; // ZQMJ 执勤民警  VARCHAR2（30） 否

    private String   wfsj;  //WFSJ 违法时间  DATETIME（8） 否

    private  String  wfdd;  //WFDD 交通违法地点编码  VARCHAR2（5） 否

    private String clbj; // CLBJ 处理标记  VARCHAR2（1） 否

    private String  ydclbj;  //YDCLBJ 异地处理标记（0-未异地处理;1-本地处理本省外地市记录;2-本地处理外省记录;3-本省外地市处理本地记录;4-外省处理本地记录;5-发现地处理记录）  CHAR（1） 否

    private String  wfdz;   //WFDZ 违法地址  VARCHAR2（250） 否

    private String wfjl_cljgmc;   //WFJL_CLJGMC 处理机关名称  VARCHAR2（200） 否

    private String  cjjgmc;    //CJJGMC 采集机关名称  VARCHAR2（200） 否

    private String   jdchpzldm;   //JDCHPZLDM  机动车号牌代码

    private String wzyy;    //违法原因

    private int kf;   //违法行为所扣分

    private String cjjg; //采集机构代码

    public String getJdcsyrmc() {
        return jdcsyrmc;
    }

    public void setJdcsyrmc(String jdcsyrmc) {
        this.jdcsyrmc = jdcsyrmc;
    }

    public String getJdchphm() {
        return jdchphm;
    }

    public void setJdchphm(String jdchphm) {
        this.jdchphm = jdchphm;
    }

    public String getDsrmc() {
        return dsrmc;
    }

    public void setDsrmc(String dsrmc) {
        this.dsrmc = dsrmc;
    }

    public String getClppzw() {
        return clppzw;
    }

    public void setClppzw(String clppzw) {
        this.clppzw = clppzw;
    }

    public String getJtwfxwdm() {
        return jtwfxwdm;
    }

    public void setJtwfxwdm(String jtwfxwdm) {
        this.jtwfxwdm = jtwfxwdm;
    }

    public String getCldxbj() {
        return cldxbj;
    }

    public void setCldxbj(String cldxbj) {
        this.cldxbj = cldxbj;
    }

    public String getZqmj() {
        return zqmj;
    }

    public void setZqmj(String zqmj) {
        this.zqmj = zqmj;
    }

    public String getWfsj() {
        return wfsj;
    }

    public void setWfsj(String wfsj) {
        this.wfsj = wfsj;
    }

    public String getWfdd() {
        return wfdd;
    }

    public void setWfdd(String wfdd) {
        this.wfdd = wfdd;
    }

    public String getWzyy() {
        return wzyy;
    }

    public void setWzyy(String wzyy) {
        this.wzyy = wzyy;
    }

    public String getFkje() {
        return fkje;
    }

    public void setFkje(String fkje) {
        this.fkje = fkje;
    }

    public String getClbj() {
        return clbj;
    }

    public void setClbj(String clbj) {
        this.clbj = clbj;
    }

    public String getYdclbj() {
        return ydclbj;
    }

    public void setYdclbj(String ydclbj) {
        this.ydclbj = ydclbj;
    }

    public String getWfjl_cljgmc() {
        return wfjl_cljgmc;
    }

    public void setWfjl_cljgmc(String wfjl_cljgmc) {
        this.wfjl_cljgmc = wfjl_cljgmc;
    }

    public String getWfdz() {
        return wfdz;
    }

    public void setWfdz(String wfdz) {
        this.wfdz = wfdz;
    }

    public String getCjjgmc() {
        return cjjgmc;
    }

    public void setCjjgmc(String cjjgmc) {
        this.cjjgmc = cjjgmc;
    }

    public String getJdchpzldm() {
        return jdchpzldm;
    }

    public void setJdchpzldm(String jdchpzldm) {
        this.jdchpzldm = jdchpzldm;
    }

    public int getKf() {
        return kf;
    }

    public void setKf(int kf) {
        this.kf = kf;
    }

    public String getCjjg() {
        return cjjg;
    }

    public void setCjjg(String cjjg) {
        this.cjjg = cjjg;
    }
}
