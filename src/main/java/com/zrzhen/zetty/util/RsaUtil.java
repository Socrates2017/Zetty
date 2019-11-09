package com.zrzhen.zetty.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Java RSA 加密工具类
 * 参考： https://blog.csdn.net/qy20115549/article/details/83105736
 * <p>
 * RSA的密钥长度决定了可以加密的明文长度，如果明文超长会出现如下报错:
 * 一次能加密的明文长度与密钥长度成正比： len_in_byte(raw_data) = len_in_bit(key)/8 -11
 * 例如
 * 1024bit 密钥 能加密明文最大的长度是 1024/8 -11 = 117 byte
 * 2048bit 密钥 能加密明文最大的长度是 2048/8 -11 = 245 byte
 * <p>
 * 密钥长度决定了:
 * 1. 支持的明文长度越长
 * 2. 加解密损耗时间越长
 * 3. 破解起来复杂度越大
 * <p>
 * 简单来说就是非对称加密，公钥可以直接放在H5，APP等前端程序中，即使被拿到，想要用公钥破解出私钥也是极难的。
 * <p>
 * 先决定密钥长度后生成一套一对一关系的公私钥。
 * 公钥提供给前端，私钥放在服务端。
 * 通过RSA公钥加密明文，加密后的密文发到服务端，服务端用RSA私钥解密得出明文。
 * @author chenanlian
 */
public class RsaUtil {


    /**
     * 随机生成密钥对
     */
    public static Map genKeyPair(int keySize) throws NoSuchAlgorithmException {

        Map<String, String> keyMap = new HashMap<String, String>();
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器
        keyPairGen.initialize(keySize, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        // 将公钥和私钥保存到Map
        //0表示公钥
        keyMap.put("public", publicKeyString);
        //1表示私钥
        keyMap.put("private", privateKeyString);
        return keyMap;
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(str);
        //base64编码的私钥
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static void main(String[] args) throws Exception {
        long temp = System.currentTimeMillis();
        //生成公钥和私钥
        Map<String, String> keyMap = genKeyPair(1024);
        //加密字符串
        System.out.println("公钥:");
        System.out.println(keyMap.get("public"));
        System.out.println("私钥:");
        System.out.println(keyMap.get("private"));
        System.out.println("生成密钥消耗时间:" +(System.currentTimeMillis() - temp)+ "ms");
        String message = "zrzhen";
        System.out.println("原文:" + message);
        temp = System.currentTimeMillis();
        String messageEn = encrypt(message, keyMap.get("public"));
        System.out.println("密文:" + messageEn);
        System.out.println("加密消耗时间:" + (System.currentTimeMillis() - temp)  + "ms");
        temp = System.currentTimeMillis();
        String messageDe = decrypt(messageEn, keyMap.get("private"));
        System.out.println("解密:" + messageDe);
        System.out.println("解密消耗时间:" + (System.currentTimeMillis() - temp) + "ms");
    }
}
