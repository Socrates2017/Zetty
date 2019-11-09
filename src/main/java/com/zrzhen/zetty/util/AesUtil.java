package com.zrzhen.zetty.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 跟mysql内置aes加解密方法一致
 * <p>
 * mysql中aes加解密函数使用示例
 * 加密：select HEX(AES_ENCRYPT('138000138000', 'key'))
 * 解密：select AES_DECRYPT(UNHEX('189443C6FB32431CEEAD702638A619BD'), 'key')
 *
 * 批量更新
 *
 * 批量从row_data字段加密到encrypt字段：
 * UPDATE codec a set a.encrypt =HEX(AES_ENCRYPT(a.row_data, 'key'))
 *
 * 批量从encrypt字段解密到decrypt字段：U
 * PDATE codec a set a.decrypt =AES_DECRYPT(UNHEX(a.encrypt), 'key')
 *
 * @author chenanlian
 */

public class AesUtil {

    private static final Logger log = LoggerFactory.getLogger(AesUtil.class);


    /**
     * 加密
     *
     * @param data 数据
     * @param key  秘钥
     * @return
     */
    public static String encrypt(String data, String key) {
        try {
            SecretKey secretKey = generateMySQLAESKey(key, "ASCII");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cleartext = data.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(Hex.encodeHex(ciphertextBytes));

        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            log.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     */
    public static String decrypt(String data, String key) {
        try {
            SecretKey secretKey = generateMySQLAESKey(key, "ASCII");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cleartext = Hex.decodeHex(data.toCharArray());
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(ciphertextBytes, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            log.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (DecoderException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    public static SecretKeySpec generateMySQLAESKey(final String key, final String encoding) throws UnsupportedEncodingException {

        final byte[] finalKey = new byte[16];
        int i = 0;
        for (byte b : key.getBytes(encoding)) {
            finalKey[i++ % 16] ^= b;
        }
        return new SecretKeySpec(finalKey, "AES");

    }

    public static void main(String[] args) {
        String abc = "138000138000";
        String aeskey = "ppmoney";
        String a1 = encrypt(abc, aeskey);
        System.out.println(a1.toUpperCase());
        System.out.println(decrypt(a1, aeskey));
    }


}