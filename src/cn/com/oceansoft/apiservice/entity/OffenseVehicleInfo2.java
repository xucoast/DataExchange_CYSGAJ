package cn.com.oceansoft.apiservice.entity;


/**
 * Created by smc on 2016/2/19.
 * 违法车辆实体类
 */
public class OffenseVehicleInfo2 {

    private String clsj;     //CLSJ 处理时间

    private String  dllklddm; //DLLKLDDM 道路路口路段代码 VARCHAR2

    private  String  fkje; //FKJE 罚款金额 NUMBER

    private   String gddh; ///GDDH 固定电话 VARCHAR2

    private String  jbr1; //JBR1 经办人1 VARCHAR2

    private String  jbr2; //JBR1 经办人2 VARCHAR2

    private String  jdchphm;   //JDCHPHM 机动车号牌号码 VARCHAR2

    private String  jdchpzldm;  //JDCHPZLDM 机动车号牌种类代码 VARCHAR2

    private  String  jdcsyrmc; //JDCSYRMC 机动车所有人名称 VARCHAR2

    private String  jkfs; //JKFS 交款方式 VARCHAR2

    private String jkrq;   //JKRQ 缴款日期 DATE

    private String jsjg;  //JSJG 接受机关 VARCHAR2

    private String  lrr;  //LRR 录入人 VARCHAR2

    private String lrsj;   //LRSJ 录入时间 DATETIME

    private String  lxfs; //LXFS 联系方式 VARCHAR2

    private String  nl;  //NL 年龄 VARCHAR2

    private String sgdj; // SGDJ 事故等级 CHAR

    private String wfdz; //WFDZ 违法地址 VARCHAR2

    private String wfsj; //WFSJ 违法时间 DATE

    private String  zjmc; //ZJMC 证件名称 VARCHAR2

    private String  wzyy;   //违章原因

    public String getClsj() {
        return clsj;
    }

    public void setClsj(String clsj) {
        this.clsj = clsj;
    }

    public String getDllklddm() {
        return dllklddm;
    }

    public void setDllklddm(String dllklddm) {
        this.dllklddm = dllklddm;
    }

    public String getFkje() {
        return fkje;
    }

    public void setFkje(String fkje) {
        this.fkje = fkje;
    }

    public String getGddh() {
        return gddh;
    }

    public void setGddh(String gddh) {
        this.gddh = gddh;
    }

    public String getJbr1() {
        return jbr1;
    }

    public void setJbr1(String jbr1) {
        this.jbr1 = jbr1;
    }

    public String getJbr2() {
        return jbr2;
    }

    public void setJbr2(String jbr2) {
        this.jbr2 = jbr2;
    }

    public String getJdchphm() {
        return jdchphm;
    }

    public void setJdchphm(String jdchphm) {
        this.jdchphm = jdchphm;
    }

    public String getJdchpzldm() {
        return jdchpzldm;
    }

    public void setJdchpzldm(String jdchpzldm) {
        this.jdchpzldm = jdchpzldm;
    }

    public String getJdcsyrmc() {
        return jdcsyrmc;
    }

    public void setJdcsyrmc(String jdcsyrmc) {
        this.jdcsyrmc = jdcsyrmc;
    }

    public String getJkfs() {
        return jkfs;
    }

    public void setJkfs(String jkfs) {
        this.jkfs = jkfs;
    }

    public String getJkrq() {
        return jkrq;
    }

    public void setJkrq(String jkrq) {
        this.jkrq = jkrq;
    }

    public String getJsjg() {
        return jsjg;
    }

    public void setJsjg(String jsjg) {
        this.jsjg = jsjg;
    }

    public String getLrr() {
        return lrr;
    }

    public void setLrr(String lrr) {
        this.lrr = lrr;
    }

    public String getLrsj() {
        return lrsj;
    }

    public void setLrsj(String lrsj) {
        this.lrsj = lrsj;
    }

    public String getLxfs() {
        return lxfs;
    }

    public void setLxfs(String lxfs) {
        this.lxfs = lxfs;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    public String getSgdj() {
        return sgdj;
    }

    public void setSgdj(String sgdj) {
        this.sgdj = sgdj;
    }

    public String getWfdz() {
        return wfdz;
    }

    public void setWfdz(String wfdz) {
        this.wfdz = wfdz;
    }

    public String getWfsj() {
        return wfsj;
    }

    public void setWfsj(String wfsj) {
        this.wfsj = wfsj;
    }

    public String getZjmc() {
        return zjmc;
    }

    public void setZjmc(String zjmc) {
        this.zjmc = zjmc;
    }

    public String getWzyy() {
        return wzyy;
    }

    public void setWzyy(String wzyy) {
        this.wzyy = wzyy;
    }
}
