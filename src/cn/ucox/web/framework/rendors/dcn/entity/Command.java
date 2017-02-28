/**
 * cn.ucox.web.logservice.rendors.orcldcn.entity.Command
 *
 * @author chenw
 * @create 16/2/23.13:31
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.entity;

import oracle.jdbc.dcn.RowChangeDescription;

/**
 * 任务命令对象
 *
 * @author chenw
 * @create 16/2/23 13:31
 * @email javacspring@gmail.com
 */
public class Command {

    private String action;

    private RowChangeDescription.RowOperation operation;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public RowChangeDescription.RowOperation getOperation() {
        return operation;
    }

    public void setOperation(RowChangeDescription.RowOperation operation) {
        this.operation = operation;
    }
}
