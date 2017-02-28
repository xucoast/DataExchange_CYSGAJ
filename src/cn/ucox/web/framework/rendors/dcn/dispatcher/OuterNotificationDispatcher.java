/**
 * cn.ucox.web.logservice.rendors.orcldcn.dcn.InnerNotificationDispatcher
 *
 * @author chenw
 * @create 16/2/19.14:22
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.dispatcher;

import cn.ucox.web.framework.rendors.dcn.TaskExecutor;
import cn.ucox.web.framework.rendors.dcn.entity.RespCmd;
import cn.ucox.web.framework.rendors.dcn.processor.IProcessor;
import oracle.jdbc.dcn.RowChangeDescription.RowOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Oracle DCN外网数据处理调度器
 *
 * @author chenw
 * @create 16/2/19 14:22
 * @email javacspring@gmail.com
 */
public abstract class OuterNotificationDispatcher implements IDispatchable, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OuterNotificationDispatcher.class);
    //业务处理执行器
    private static final TaskExecutor TASK_EXECUTOR = TaskExecutor.getInstance();

    //DCN事件数据库行记录ID
    private volatile String rowId;

    @Override
    public void dispatch(String tableName, String id, RowOperation rowOperation) {
        rowId = id;
        TASK_EXECUTOR.post(this);
    }

    @Override
    public void run() {
        logger.debug("===================线程[{}]处理请求开始===================", Thread.currentThread().getId());
        //1.根据ROWID加载请求命令信息
        RespCmd reqCmd = loadReqCmd(rowId);
        if (null == reqCmd) {
            return;
        }
        //2.校验是否已经注册相应插件处理此请求
        IProcessor processor = lookupPlugin(reqCmd);
        if (null == processor) {
            return;
        }
        //3.调用插件结果处理逻辑
        processor.doResponse(reqCmd);
        //4.删除结果表数据
        doPersistence(reqCmd);
        //5.重置ReqCmd
        resetReqCmd();
        logger.debug("===================线程[{}]处理请求结束===================\n", Thread.currentThread().getId());
    }


    @Override
    public Properties getDCNProperties() {
        return getProperties();
    }

    protected abstract Properties getProperties();

    /**
     * 从前置机加载查询命令对象
     *
     * @param rowId 监听数据通知行记录ID
     * @return ReqCmd
     */
    protected abstract RespCmd loadReqCmd(String rowId);

    /**
     * 根据查询命令从插件注册表中加载已注册插件
     *
     * @param respCmd 查询命令
     * @return 实现IProcessor接口插件实例
     */
    protected abstract IProcessor lookupPlugin(RespCmd respCmd);

    /**
     * 持久化插件返回RespCmd实例数据
     *
     * @param respCmd 响应命令
     */
    protected abstract void doPersistence(RespCmd respCmd);

    /**
     * 查询命令重置
     */
    protected abstract void resetReqCmd();

}
