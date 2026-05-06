package com.halo.utils;

import java.security.MessageDigest;

/**
 * @Description: MD5加密解密
 * @Author: liuruyi
 * @Date: 2026/5/6 16:55
 */
public class MD5Util {

    public final static String getMD5(String s){

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd','e', 'f'};
        try {
            byte[] strTemp = s.getBytes("utf-8");

            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>>4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
