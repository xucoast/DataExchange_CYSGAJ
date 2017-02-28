/**
 * cn.ucox.web.logservice.rendors.orcldcn.entity.ReqCmd
 *
 * @author chenw
 * @create 16/2/19.17:54
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.entity;

import oracle.jdbc.dcn.RowChangeDescription.RowOperation;

/**
 * 请求命令
 *
 * @author chenw
 * @create 16/2/19 17:54
 * @email javacspring@gmail.com
 */
public class ReqCmd extends ApiReq {

    private RowOperation operation;

    public RowOperation getOperation() {
        return operation;
    }

    public void setOperation(RowOperation operation) {
        this.operation = operation;
    }
}
