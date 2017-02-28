/**
 * cn.ucox.web.logservice.rendors.orcldcn.entity.ApiReq
 *
 * @author chenw
 * @create 16/2/22.10:54
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.entity;

import java.util.Date;

/**
 * 对外服务查询命令
 *
 * @author chenw
 * @create 16/2/22 10:54
 * @email javacspring@gmail.com
 */
public class ApiReq extends Command {

    private String serialNum;
    private String command;
    private String hash;
    private boolean async;
    private String reqIP;
    private Date reqStart;
    private long reqTimeout;

    public long getReqTimeout() {
        return reqTimeout;
    }

    public void setReqTimeout(long reqTimeout) {
        this.reqTimeout = reqTimeout;
    }

    public Date getReqStart() {
        return reqStart;
    }

    public void setReqStart(Date reqStart) {
        this.reqStart = reqStart;
    }

    public String getReqIP() {
        return reqIP;
    }

    public void setReqIP(String reqIP) {
        this.reqIP = reqIP;
    }

    public String getHash() {
        return hash;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
