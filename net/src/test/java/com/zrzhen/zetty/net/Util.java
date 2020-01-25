package com.zrzhen.zetty.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Util {


    public static String inputStream2String(InputStream inputStream) {
        byte[] bytes = new byte[0];
        try {
            bytes = new byte[inputStream.available()];
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    /**
     * 字符串转字节数组
     *
     * @param str      要转换的字符串
     * @param encoding 编码
     * @return
     */
    public static byte[] str2Byte(String str, String encoding) {
        try {
            byte[] bytes;
            if (encoding != null) {
                bytes = str.getBytes(encoding);
            } else {
                bytes = str.getBytes();
            }
            return bytes;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 字符串转字节数组，默认utf-8编码
     *
     * @param str
     * @return
     */
    public static byte[] str2Byte(String str) {
        return str2Byte(str, "utf-8");
    }


    /**
     * 字节数组转字符串
     *
     * @param bytes
     * @param encoding
     * @return
     */
    public static String byte2Str(byte[] bytes, String encoding) {
        try {
            String out;
            if (encoding != null) {
                out = new String(bytes, encoding);
            } else {
                out = new String(bytes);
            }
            return out;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 字节数组转字符串，默认utf-8编码
     *
     * @param bytes
     * @return
     */
    public static String byte2Str(byte[] bytes) {
        return byte2Str(bytes, "utf-8");
    }
}
