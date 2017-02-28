/**
 * cn.ucox.web.framework.rendors.dcn.utils.StringUtil
 *
 * @author chenw
 * @create 16/2/23.11:28
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * DCN字符处理工具类
 *
 * @author chenw
 * @create 16/2/23 11:28
 * @email javacspring@gmail.com
 */
public class StringUtil {

    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException var2) {
            throw new RuntimeException(var2.getMessage());
        }
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !StringUtil.isBlank(str);
    }

    /**
     * 生成请求参数签名信息
     *
     * @param data 请求参数(JSON)
     * @return hash
     */
    public static String md5Hex(String data) {
        return new String(encodeHex(getDigest("MD5").digest(data.getBytes())));
    }

    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;
        for (int j = 0; i < l; ++i) {
            out[j++] = DIGITS[(240 & data[i]) >>> 4];
            out[j++] = DIGITS[15 & data[i]];
        }
        return out;
    }
}
