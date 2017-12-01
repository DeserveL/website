package com.deservel.website.common.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * aes工具类
 *
 * @author DeserveL
 * @date 2017/12/1 14:42
 * @since 1.0.0
 */
public interface AesUtils {

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    static String enAes(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return new BASE64Encoder().encode(encryptedBytes);
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    static String deAes(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] cipherTextBytes = new BASE64Decoder().decodeBuffer(data);
        byte[] decValue = cipher.doFinal(cipherTextBytes);
        return new String(decValue);
    }
}
