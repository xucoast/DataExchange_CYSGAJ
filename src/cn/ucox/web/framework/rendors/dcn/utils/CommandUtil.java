/**
 * cn.ucox.web.framework.rendors.dcn.utils.CommandUtil
 *
 * @author chenw
 * @create 16/2/23.16:17
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.utils;

/**
 * @author chenw
 * @create 16/2/23 16:17
 * @email javacspring@gmail.com
 */
public class CommandUtil {

    /**
     * 获取命令HASH值
     *
     * @param commandData 命令参数
     * @return 命令HASH值
     */
    public static String hash(String commandData) {
        if (StringUtil.isBlank(commandData))
            return "";
        return StringUtil.md5Hex(commandData);
    }
}
