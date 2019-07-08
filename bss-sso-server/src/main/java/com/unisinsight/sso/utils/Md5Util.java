package com.unisinsight.sso.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: yangxiaoyu
 * @Date: 2019/6/27 11:19
 */
public final class Md5Util {
    public static String encodeMD5(String src) throws NoSuchAlgorithmException {
        // 加密对象，指定加密方式
        MessageDigest md5 = MessageDigest.getInstance("md5");
        // 准备要加密的数据
        byte[] b = src.getBytes();
        // 加密
        byte[] digest = md5.digest(b);
        // 十六进制的字符
        char[] chars = new char[] { '0', '1', '2', '3', '4', '5',
                '6', '7' , '8', '9', 'a', 'b', 'c', 'd', 'e','f' };
        StringBuffer sb = new StringBuffer();
        // 处理成十六进制的字符串(通常)
        for (byte bb : digest) {
            sb.append(chars[(bb >> 4) & 15]);
            sb.append(chars[bb & 15]);
        }
        // 打印加密后的字符串
        return sb.toString();
    }
}
