package com.github.lazylibrary.util;

import android.util.Log;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 作者：Administrator on 2016/8/22 13:57
 * 邮箱：906514731@qq.com
 */
public class AES {

    /**加密
     * @throws Exception
     */
    public static String Encrypt(String sSrc, String sKey) throws Exception {

        if (sKey == null) {
            Log.e("Key为空null","Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            Log.e("\"Key长度不是16位\"","Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec("1234567890123456".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());

        return Base64_2.encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }


    // 解密
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                Log.e("Key为空null","Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                Log.e("\"Key长度不是16位\"","Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("1234567890123456".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64_2.decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
}