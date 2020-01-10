package com.zrzhen.zetty.p2p.util;

import com.zrzhen.zetty.common.FileUtil;

import java.nio.ByteBuffer;

/**
 * @author chenanlian
 */
public class ByteUtil {


    /**
     * 消息编码
     *
     * @param msg
     * @return
     */
    public static ByteBuffer msgEncode(String msg) {
        byte[] payload = FileUtil.str2Byte(msg);
        byte[] header = int2byte(payload.length);
        ByteBuffer buf = ByteBuffer.allocate(header.length + payload.length);
        buf.put(header);
        buf.put(payload);
        return buf;
    }

    /**
     * 获取消息体长度
     *
     * @param byteBuffer
     * @return
     */
    public static int msgLength(ByteBuffer byteBuffer) {
        byte[] bytes = FileUtil.buf2Bytes(byteBuffer, 4);
        int length = byte2int(bytes);
        return length;
    }

    public static byte[] msgBytes(ByteBuffer byteBuffer, int length) {
        byte[] bytes = FileUtil.buf2Bytes(byteBuffer, length);
        return bytes;
    }

    public static String msgDecode(ByteBuffer byteBuffer) {
        byte[] bytes = byteBuffer.array();
        String msg = FileUtil.byte2Str(subBytes(bytes, 3, bytes.length - 3));
        return msg;
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }


    /**
     * int 转成byte数组
     */
    public static byte[] int2byte(int id) {
        //int是32位 4个字节  创建length为4的byte数组
        byte[] arr = new byte[4];
        arr[0] = (byte) ((id >> 0 * 8) & 0xff);
        arr[1] = (byte) ((id >> 1 * 8) & 0xff);
        arr[2] = (byte) ((id >> 2 * 8) & 0xff);
        arr[3] = (byte) ((id >> 3 * 8) & 0xff);
        return arr;
    }

    /**
     * byte数组  转回int
     */
    public static int byte2int(byte[] arr) {
        int i0 = (arr[0] & 0xff) << 0 * 8;
        int i1 = (arr[1] & 0xff) << 1 * 8;
        int i2 = (arr[2] & 0xff) << 2 * 8;
        int i3 = (arr[3] & 0xff) << 3 * 8;
        return i0 + i1 + i2 + i3;
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

}
