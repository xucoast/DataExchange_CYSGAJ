package cn.ucox.web.framework.rendors.dcn.processor;

import cn.ucox.web.framework.rendors.dcn.entity.ReqCmd;
import cn.ucox.web.framework.rendors.dcn.entity.RespCmd;

/**
 * 业务处理器接口,
 * 扩展服务插件必须实现此接口
 *
 * @author chenw
 * @create 16/2/19.23:37
 * @email javacspring@hotmail.com
 */
public interface IProcessor {

    /**
     * 处理器处理命令标识
     *
     * @return String 处理响应命令标识
     */
    String command();

    /**
     * 内网查询结果业务回调
     *
     * @param reqCmd 请求执行命令
     * @return RespCmd 响应命令对象
     */
    RespCmd doQuery(ReqCmd reqCmd);

    /**
     * 外网查询结果业务处理回调
     *
     * @param resCmd 响应命令对象
     */
    void doResponse(RespCmd resCmd);
}
