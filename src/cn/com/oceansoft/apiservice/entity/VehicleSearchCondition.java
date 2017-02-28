package cn.com.oceansoft.apiservice.entity;

/**
 * Created by smc on 2016/2/19.
 * 车辆查询条件
 */
public class VehicleSearchCondition {

    private String jdc_syr; // JDC_SYR 等于, JDC_SYR 机动车所有人名称 VARCHAR2

    private String jdchphm;  //  模糊匹配  ,JDCHPHM 机动车号牌号码 VARCHAR2

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
}
