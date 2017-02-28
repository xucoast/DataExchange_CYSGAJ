package cn.com.oceansoft.apiservice.entity;

/**
 * cn.com.oceansoft.apiservice.entity
 * Created by smc
 * date on 2016/2/24.
 * Email:sunmch@163.com
 * 已决在押人员实体类
 */
public class YjZyPersonInfo {

    private  String   ay; //AY 案由  VARCHAR2（4000）

    private String  kssmc;  //KSSMC 看守所名称  VARCHAR2（4000）

    private String  rsrq; //RSRQ 入所日期  DATE（3）

    private String rsxz; //RSXZ 入所性质  VARCHAR2（4000）

    private String  sf; //SF 身份  VARCHAR2（4000）

    private  String  xm; //XM 姓名  VARCHAR2（50）

    private String  zy; //ZY 职业  VARCHAR2（4000）

    private String  mzmc;  //MZMC 民族名称  VARCHAR2（4000）

    private String jyrq;  // JYRQ 羁押日期  DATE（3）

    private String  jsh; //JSH 监室号  VARCHAR2（20）

    private String kssbh; //KSSBH 看守所编号  VARCHAR2（18）

    private String cszt;  //CSZT 出所状态  VARCHAR2（50）

    private String csrq;  // CSRQ 出所日期  DATE（3）

    private String cjsj;   //CJSJ 创建时间  DATE（3）

    private String  zjhm; //ZJHM 证件号码  VARCHAR2（60）

    private String  cyzjlx; //CYZJLX 证件类型名称  VARCHAR2（50）

    private String  fwcs; //FWCS 工作单位  VARCHAR2（50）

    private String  hjdmc;  // HJDMC 户籍地详址  VARCHAR2（120）

    private String   hyzkdm;  //HYZKDM 婚姻状况(汉字)  VARCHAR2（50）

    private String whcd; // WHCD 学历名称(中文)  VARCHAR2（50）

    private String  xzzxq; //XZZXQ 现住地详址  VARCHAR2（250）

    private String yqtxqx; //YQTXQX 关押期限  DATE（3）

    public String getAy() {
        return ay;
    }

    public void setAy(String ay) {
        this.ay = ay;
    }

    public String getKssmc() {
        return kssmc;
    }

    public void setKssmc(String kssmc) {
        this.kssmc = kssmc;
    }

    public String getRsrq() {
        return rsrq;
    }

    public void setRsrq(String rsrq) {
        this.rsrq = rsrq;
    }

    public String getRsxz() {
        return rsxz;
    }

    public void setRsxz(String rsxz) {
        this.rsxz = rsxz;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getMzmc() {
        return mzmc;
    }

    public void setMzmc(String mzmc) {
        this.mzmc = mzmc;
    }

    public String getJyrq() {
        return jyrq;
    }

    public void setJyrq(String jyrq) {
        this.jyrq = jyrq;
    }

    public String getJsh() {
        return jsh;
    }

    public void setJsh(String jsh) {
        this.jsh = jsh;
    }

    public String getKssbh() {
        return kssbh;
    }

    public void setKssbh(String kssbh) {
        this.kssbh = kssbh;
    }

    public String getCszt() {
        return cszt;
    }

    public void setCszt(String cszt) {
        this.cszt = cszt;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getCjsj() {
        return cjsj;
    }

    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getCyzjlx() {
        return cyzjlx;
    }

    public void setCyzjlx(String cyzjlx) {
        this.cyzjlx = cyzjlx;
    }

    public String getFwcs() {
        return fwcs;
    }

    public void setFwcs(String fwcs) {
        this.fwcs = fwcs;
    }

    public String getHjdmc() {
        return hjdmc;
    }

    public void setHjdmc(String hjdmc) {
        this.hjdmc = hjdmc;
    }

    public String getHyzkdm() {
        return hyzkdm;
    }

    public void setHyzkdm(String hyzkdm) {
        this.hyzkdm = hyzkdm;
    }

    public String getWhcd() {
        return whcd;
    }

    public void setWhcd(String whcd) {
        this.whcd = whcd;
    }

    public String getXzzxq() {
        return xzzxq;
    }

    public void setXzzxq(String xzzxq) {
        this.xzzxq = xzzxq;
    }

    public String getYqtxqx() {
        return yqtxqx;
    }

    public void setYqtxqx(String yqtxqx) {
        this.yqtxqx = yqtxqx;
    }
}
