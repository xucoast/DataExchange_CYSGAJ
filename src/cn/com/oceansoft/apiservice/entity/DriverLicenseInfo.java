package cn.com.oceansoft.apiservice.entity;

/**
 * cn.com.oceansoft.apiservice.entity
 * Created by smc
 * date on 2016/3/2.
 * Email:sunmch@163.com
 * ji驾照信息实体类
 */
public class DriverLicenseInfo {

    private  String xm;

    private String xb;    //性别  2:女  1：男

    private String gj;    // 国籍

    private String lxzsxxdz;  // 联系住所详细地址

    private String sfzmmc;    // 身份证明名称

    private String sfzmhm;    // 身份证明号码

    private String  jxmc;  //JXMC 驾校名称  VARCHAR2（410）

    private String  jtglywdxsfzhm;   //JTGLYWDXSFZMHM 交通管理业务对象身份证明号码  VARCHAR2（40）

    private  String jbr;   //JBR 经办人  VARCHAR2（60）

    private String  gxsj;  //GXSJ 更新时间  DATETIME（8）

    private String  fzrq;//FZRQ 发证日期  DATETIME（8）

    private String  dzyx; //DZYX 电子邮箱  VARCHAR2（60）

    private String   djzsxxdz;  //DJZSXXDZ 登记住所详细地址  VARCHAR2（400）

    private String dabh;  // DABH 机动车档案编号  CHAR（30）

    private String csrq;   //CSRQ 出生日期  DATETIME（8）

    private String jdcjszcfrq;   //JDCJSZCFRQ 机动车驾驶证超分日期  DATETIME（8）

    private String cclzrq;  //CCLZRQ 初次领证日期  DATETIME（8）

    private String  ccfzjg;  //CCFZJG 初次发证机关  VARCHAR2（20）

    private String  bz_cs; //BZ_CS 补证次数  NUMBER（0）

    private String  syyxqz; //SYYXQZ 审验有效期止  DATETIME（8）

    private String  lxdh; //LXDH 联系电话  VARCHAR2（40）

    private String  jdcjszljjf; //JDCJSZLJJF 机动车驾驶证累积记分  NUMBER（0）  其实就扣分

    private String  yxqs; //YXQS 有效期始  DATETIME（8）

    private String  zjcx; //ZJCX 准驾车型  VARCHAR2（40）

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getJxmc() {
        return jxmc;
    }

    public void setJxmc(String jxmc) {
        this.jxmc = jxmc;
    }

    public String getJtglywdxsfzhm() {
        return jtglywdxsfzhm;
    }

    public void setJtglywdxsfzhm(String jtglywdxsfzhm) {
        this.jtglywdxsfzhm = jtglywdxsfzhm;
    }

    public String getJbr() {
        return jbr;
    }

    public void setJbr(String jbr) {
        this.jbr = jbr;
    }

    public String getGxsj() {
        return gxsj;
    }

    public void setGxsj(String gxsj) {
        this.gxsj = gxsj;
    }

    public String getFzrq() {
        return fzrq;
    }

    public void setFzrq(String fzrq) {
        this.fzrq = fzrq;
    }

    public String getDzyx() {
        return dzyx;
    }

    public void setDzyx(String dzyx) {
        this.dzyx = dzyx;
    }

    public String getDjzsxxdz() {
        return djzsxxdz;
    }

    public void setDjzsxxdz(String djzsxxdz) {
        this.djzsxxdz = djzsxxdz;
    }

    public String getDabh() {
        return dabh;
    }

    public void setDabh(String dabh) {
        this.dabh = dabh;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getJdcjszcfrq() {
        return jdcjszcfrq;
    }

    public void setJdcjszcfrq(String jdcjszcfrq) {
        this.jdcjszcfrq = jdcjszcfrq;
    }

    public String getCclzrq() {
        return cclzrq;
    }

    public void setCclzrq(String cclzrq) {
        this.cclzrq = cclzrq;
    }

    public String getCcfzjg() {
        return ccfzjg;
    }

    public void setCcfzjg(String ccfzjg) {
        this.ccfzjg = ccfzjg;
    }

    public String getBz_cs() {
        return bz_cs;
    }

    public void setBz_cs(String bz_cs) {
        this.bz_cs = bz_cs;
    }

    public String getSyyxqz() {
        return syyxqz;
    }

    public void setSyyxqz(String syyxqz) {
        this.syyxqz = syyxqz;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getJdcjszljjf() {
        return jdcjszljjf;
    }

    public void setJdcjszljjf(String jdcjszljjf) {
        this.jdcjszljjf = jdcjszljjf;
    }

    public String getYxqs() {
        return yxqs;
    }

    public void setYxqs(String yxqs) {
        this.yxqs = yxqs;
    }

    public String getZjcx() {
        return zjcx;
    }

    public void setZjcx(String zjcx) {
        this.zjcx = zjcx;
    }

    public String getGj() {
        return gj;
    }

    public void setGj(String gj) {
        this.gj = gj;
    }

    public String getLxzsxxdz() {
        return lxzsxxdz;
    }

    public void setLxzsxxdz(String lxzsxxdz) {
        this.lxzsxxdz = lxzsxxdz;
    }

    public String getSfzmmc() {
        return sfzmmc;
    }

    public void setSfzmmc(String sfzmmc) {
        this.sfzmmc = sfzmmc;
    }

    public String getSfzmhm() {
        return sfzmhm;
    }

    public void setSfzmhm(String sfzmhm) {
        this.sfzmhm = sfzmhm;
    }
}
