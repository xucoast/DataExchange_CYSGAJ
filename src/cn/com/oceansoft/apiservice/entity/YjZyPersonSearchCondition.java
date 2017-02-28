package cn.com.oceansoft.apiservice.entity;

/**
 * cn.com.oceansoft.apiservice.entity
 * Created by smc
 * date on 2016/2/24.
 * Email:sunmch@163.com
 * 已决在押人员搜索条件实体类
 */
public class YjZyPersonSearchCondition {
    //KSSMC 模糊匹配,XM 等于//信息查询条件

    private String  kssmc;  //看守所名称

    private String xm;

    public String getKssmc() {
        return kssmc;
    }

    public void setKssmc(String kssmc) {
        this.kssmc = kssmc;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }
}
