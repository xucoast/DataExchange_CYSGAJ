package cn.com.oceansoft.apiservice.entity;

/**
 * cn.com.oceansoft.apiservice.entity
 * Created by smc
 * date on 2016/2/24.
 * Email:sunmch@163.com
 * 驾驶员信息实体类
 */
public class DriverInfo {
    private  String  xm; // XM 姓名  VARCHAR2（50）

    private String xbmc;  //XBMC 性别名称  VARCHAR2（50）

    private String  gjmc;  //GJMC 国籍名称  VARCHAR2（50）

    private String  csrq;   //CSRQ 出生日期  DATE（3）

    private String lxdh;  //LXDH 联系电话  VARCHAR2（50）

    private  String lxzsxxdz;  // LXZSXXDZ 联系住所详细地址  VARCHAR2（200）

    private String   jtglywdxsfzmmc;  //JTGLYWDXSFZMMC 交通管理业务对象身份证明名称  VARCHAR2（50）

    private  String  cjsj;  //CJSJ 创建时间  VARCHAR2（14）

    private String   gxsj; //GXSJ 更新时间  DATETIME（8）

    private String  jtglywdxsfzmhm;   //JTGLYWDXSFZMHM 交通管理业务对象身份证明号码  VARCHAR2（50）

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXbmc() {
        return xbmc;
    }

    public void setXbmc(String xbmc) {
        this.xbmc = xbmc;
    }

    public String getGjmc() {
        return gjmc;
    }

    public void setGjmc(String gjmc) {
        this.gjmc = gjmc;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getLxzsxxdz() {
        return lxzsxxdz;
    }

    public void setLxzsxxdz(String lxzsxxdz) {
        this.lxzsxxdz = lxzsxxdz;
    }

    public String getJtglywdxsfzmmc() {
        return jtglywdxsfzmmc;
    }

    public void setJtglywdxsfzmmc(String jtglywdxsfzmmc) {
        this.jtglywdxsfzmmc = jtglywdxsfzmmc;
    }

    public String getCjsj() {
        return cjsj;
    }

    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }

    public String getGxsj() {
        return gxsj;
    }

    public void setGxsj(String gxsj) {
        this.gxsj = gxsj;
    }

    public String getJtglywdxsfzmhm() {
        return jtglywdxsfzmhm;
    }

    public void setJtglywdxsfzmhm(String jtglywdxsfzmhm) {
        this.jtglywdxsfzmhm = jtglywdxsfzmhm;
    }
}
