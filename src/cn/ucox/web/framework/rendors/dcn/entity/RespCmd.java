/**
 * cn.ucox.web.logservice.rendors.orcldcn.entity.RespCmd
 *
 * @author chenw
 * @create 16/2/19.17:54
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.entity;

import java.util.Date;

/**
 * 查询结果命令封装
 *
 * @author chenw
 * @create 16/2/19 17:54
 * @email javacspring@gmail.com
 */
public class RespCmd extends Command {

    private String serialNum;
    private boolean succ;
    private String result;
    private String code;
    private String msg;
    private Date respTime;

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public boolean isSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getRespTime() {
        return respTime;
    }

    public void setRespTime(Date respTime) {
        this.respTime = respTime;
    }
}