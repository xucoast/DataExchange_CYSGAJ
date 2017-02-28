package cn.com.oceansoft.apiservice.entity;

/**
 * Created by smc on 2016/2/19.
 * 车辆信息实体类
 */
public class VehicleItem {

    private String bpcs;  //BPCS 补领号牌次数 NUMBER

    private String bxzzrq;  //BXZZRQ 终止日期

    private String  ccdjrq;  //CCDJRQ 初次登记日期 DATE

    private String  ccrq; // CCRQ 出厂日期 DATE

    private String  clly; //CLLY 车辆类型 CHAR

    private String  clpp1;  //CLPP1 中文品牌名称 VARCHAR2

    private String fprq; //FPRQ 发牌日期 DATE

    private String fzrq;   //FZRQ 发证日期 DATE

    private String  lxdh;  //LXDH 联系电话 VARCHAR2

    private String sjhm;  //SJHM 手机号码 VARCHAR2

    private String  zz_dzmc;  //ZZ_DZMC 暂住_地址名称 VARCHAR2

    private  String  jdcjbxx_jbr; //JDCJBXX_JBR 经办人 VARCHAR2

    private  String   jdc_syr;   //JDC_SYR 机动车所有人名称 VARCHAR2 ;

    private String   jdchphm;    //JDCHPHM 机动车号牌号码 VARCHAR2

    private String  jtglywdxsfzmhm;   // JTGLYWDXSFZMHM 交通管理业务对象身份证明号码 VARCHAR2

    public String getBpcs() {
        return bpcs;
    }

    public void setBpcs(String bpcs) {
        this.bpcs = bpcs;
    }

    public String getBxzzrq() {
        return bxzzrq;
    }

    public void setBxzzrq(String bxzzrq) {
        this.bxzzrq = bxzzrq;
    }

    public String getCcdjrq() {
        return ccdjrq;
    }

    public void setCcdjrq(String ccdjrq) {
        this.ccdjrq = ccdjrq;
    }

    public String getCcrq() {
        return ccrq;
    }

    public void setCcrq(String ccrq) {
        this.ccrq = ccrq;
    }

    public String getClly() {
        return clly;
    }

    public void setClly(String clly) {
        this.clly = clly;
    }

    public String getClpp1() {
        return clpp1;
    }

    public void setClpp1(String clpp1) {
        this.clpp1 = clpp1;
    }

    public String getFprq() {
        return fprq;
    }

    public void setFprq(String fprq) {
        this.fprq = fprq;
    }

    public String getFzrq() {
        return fzrq;
    }

    public void setFzrq(String fzrq) {
        this.fzrq = fzrq;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getZz_dzmc() {
        return zz_dzmc;
    }

    public void setZz_dzmc(String zz_dzmc) {
        this.zz_dzmc = zz_dzmc;
    }

    public String getJdcjbxx_jbr() {
        return jdcjbxx_jbr;
    }

    public void setJdcjbxx_jbr(String jdcjbxx_jbr) {
        this.jdcjbxx_jbr = jdcjbxx_jbr;
    }

    public String getJdc_syr() {
        return jdc_syr;
    }

    public void setJdc_syr(String jdc_syr) {
        this.jdc_syr = jdc_syr;
    }

    public String getJdchphm() {
        return jdchphm;
    }

    public void setJdchphm(String jdchphm) {
        this.jdchphm = jdchphm;
    }

    public String getJtglywdxsfzmhm() {
        return jtglywdxsfzmhm;
    }

    public void setJtglywdxsfzmhm(String jtglywdxsfzmhm) {
        this.jtglywdxsfzmhm = jtglywdxsfzmhm;
    }
}
