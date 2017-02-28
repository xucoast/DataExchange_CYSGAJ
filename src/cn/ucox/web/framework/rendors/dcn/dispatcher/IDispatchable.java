package cn.ucox.web.framework.rendors.dcn.dispatcher;

import oracle.jdbc.dcn.RowChangeDescription;

import java.util.Properties;

/**
 * DCN数据处理业务分发接口
 *
 * @author chenw
 * @create 16/2/19.14:20
 * @email javacspring@hotmail.com
 */
public interface IDispatchable {

    /**
     * 分发事件
     *
     * @param tableName    表名
     * @param rowId        行记录ID
     * @param rowOperation 事件类型
     */
    void dispatch(String tableName, String rowId, RowChangeDescription.RowOperation rowOperation);

    /**
     * 获取DCN配置属性
     *
     * @return Properties
     */
    Properties getDCNProperties();
}
