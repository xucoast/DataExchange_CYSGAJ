/**
 * cn.ucox.web.logservice.rendors.orcldcn.entity.ApiResp
 *
 * @author chenw
 * @create 16/2/22.10:57
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.entity;

/**
 * 对外服务响应命令
 *
 * @author chenw
 * @create 16/2/22 10:57
 * @email javacspring@gmail.com
 */
public class ApiResp extends RespCmd {

    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
