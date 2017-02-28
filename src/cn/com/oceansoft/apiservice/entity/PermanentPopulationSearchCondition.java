package cn.com.oceansoft.apiservice.entity;

/**
 * Created by smc on 2016/2/19.
 * 查询常住人口条件类
 */
public class PermanentPopulationSearchCondition {

    private String xm;  //XM 等于  XM 姓名 VARCHAR2
    private String gmsfhm;  //  GMSFHM 等于  GMSFHM 公民身份号码 VARCHAR2

    private String userguid;  //

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getGmsfhm() {
        return gmsfhm;
    }

    public void setGmsfhm(String gmsfhm) {
        this.gmsfhm = gmsfhm;
    }

    public String getUserguid() {
        return userguid;
    }

    public void setUserguid(String userguid) {
        this.userguid = userguid;
    }
}
