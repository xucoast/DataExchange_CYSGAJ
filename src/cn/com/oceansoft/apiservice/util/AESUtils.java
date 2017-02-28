package cn.com.oceansoft.apiservice.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    // 加密
    public static String encrypt(String sSrc, String sKey) {
    	try{
	        if (sKey==null || "".equals(sKey)) {
	            return null;
	        }
	        byte[] raw = sKey.getBytes("UTF-8");
	        if (raw.length!=16) {
	            return null;
	        }
	        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
	        IvParameterSpec iv = new IvParameterSpec("2274083615764896".getBytes("UTF-8"));
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            //自动填充为0
	        byte[] sSrcBytes = sSrc.getBytes("UTF-8");
            int sSrcBytesLength = sSrcBytes.length;
            if (sSrcBytesLength%16 != 0) {
            	sSrcBytesLength = sSrcBytesLength + (16 - (sSrcBytesLength%16));
            }
            byte[] plainText = new byte[sSrcBytesLength];
            for(int i=0;i<plainText.length;i++){
            	plainText[i] = 0;
            }
            System.arraycopy(sSrcBytes, 0, plainText, 0, sSrcBytes.length);
	        byte[] encrypted = cipher.doFinal(plainText);
//	        BASE64Encoder b64enc = new BASE64Encoder();
//	        String encryptedtext = b64enc.encode(encrypted);
//	        System.out.println(encryptedtext);
	        return byte2hex(encrypted);
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }

    // 解密
    public static String decrypt(String sSrc, String sKey) throws Exception {
        try {
	        if (sKey==null || "".equals(sKey)) {
	            return null;
	        }
	        byte[] raw = sKey.getBytes("UTF-8");
	        if (raw.length!=16) {
	            return null;
	        }
	        if(sSrc.length()%16!=0){
	        	return null;
	        }
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] decrypted = hex2byte(sSrc);
            byte[] original = cipher.doFinal(decrypted);
            String originalString = new String(original, "UTF-8");
            return originalString;
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }

    public static byte[] hex2byte(String hex) {
        if (hex == null) {
            return null;
        }
        int l = hex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static void main(String[] args) throws Exception {
        //1234567890123456
        String  desc = encrypt("123456", "1234567890123456");
        System.out.println(desc);
        //59C9D46638A67FDEEB5ADC045545F1064FFEEFB084B20CD40ABE71BFC34EF887
        String d = decrypt("59C9D46638A67FDEEB5ADC045545F1064FFEEFB084B20CD40ABE71BFC34EF887","1234567890123456");
        System.out.println(d);

    }
}