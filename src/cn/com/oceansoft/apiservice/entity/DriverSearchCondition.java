package cn.com.oceansoft.apiservice.entity;

/**
 * cn.com.oceansoft.apiservice.entity
 * Created by smc
 * date on 2016/2/24.
 * Email:sunmch@163.com
 * 查询驾驶员条件实体类
 */
public class DriverSearchCondition {
    //XM 等于,JTGLYWDXSFZMMC 等于//信息查询条件

    private String xm;  //  姓名名称

    private String jtglywdxsfzmmc;  // 交通管理业务对象身份证明名称

    private  String jtglywdxsfzhm;   //JTGLYWDXSFZMHM 交通管理业务对象身份证明号码  其实就是身份证号码

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getJtglywdxsfzmmc() {
        return jtglywdxsfzmmc;
    }

    public void setJtglywdxsfzmmc(String jtglywdxsfzmmc) {
        this.jtglywdxsfzmmc = jtglywdxsfzmmc;
    }

    public String getJtglywdxsfzhm() {
        return jtglywdxsfzhm;
    }

    public void setJtglywdxsfzhm(String jtglywdxsfzhm) {
        this.jtglywdxsfzhm = jtglywdxsfzhm;
    }
}
