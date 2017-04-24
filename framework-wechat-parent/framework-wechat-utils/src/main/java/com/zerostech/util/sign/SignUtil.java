package com.zerostech.util.sign;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

/**
 * SignUtil
 *
 * @author hzzjb
 * @date 2017/4/12
 */
public class SignUtil {
    /**
     * SHA1 安全加密算法
     * @param params 参数value list集合
     * @return
     * @throws DigestException
     */
    public static String SHA1(List<String> params) throws DigestException {
        //获取信息摘要 - 参数字典排序后字符串
        Collections.sort(params);
        String decrypt = mergeList(params);
        try {
            //指定sha1算法
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decrypt.getBytes());
            //获取字节数组
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DigestException("签名错误！");
        }
    }

    /**
     * 合并字符串List
     */
    public static String mergeList(List<String> params) {
        StringBuilder sb = new StringBuilder();
        for (String s : params) {
            sb.append(s);
        }
        return sb.toString();
    }

}
