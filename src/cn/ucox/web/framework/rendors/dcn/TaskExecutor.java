/**
 * 执行请求查询线程池
 *
 * @author chenw
 * @create 16/2/20.14:32
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 执行请求查询线程池
 *
 * @author chenw
 * @create 16/2/20 14:32
 * @email javacspring@gmail.com
 */
public class TaskExecutor {

    private static final TaskExecutor instance = new TaskExecutor();
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    public static TaskExecutor getInstance() {
        return instance;
    }

    public void post(Runnable reqProcessor) {
        executorService.submit(reqProcessor);
    }
}
