package com.zrzhen.zetty.core.util;

import com.zrzhen.zetty.core.AutoByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenanlian
 */
public class ByteUtil {

    private static Logger log = LoggerFactory.getLogger(ByteUtil.class);


    /**
     * 搜索字节数组位置，如不存在，返回-1
     *
     * @param parent
     * @param child
     * @return
     */
    public static int indexOf(byte[] parent, byte[] child) {
        for (int i = 0; i != parent.length - child.length; ++i) {
            if (parent[i] == child[0]) {
                if (Arrays.equals(Arrays.copyOfRange(parent, i, i + child.length), child)) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * 将字节数组按其从所包含的换行符\r\n处进行分组，返回分组列表
     *
     * @param bytes
     * @return
     */
    public static List<byte[]> readBytesByLine(byte[] bytes) {
        int index = 0;
        AutoByteBuffer preBuffer = AutoByteBuffer.newByteBuffer();
        List<byte[]> out = new ArrayList<>();
        while (index < bytes.length) {
            byte data = bytes[index];
            /*
             * 如果遇到要换行，则进行解析
             */
            if (isNextLine(bytes, index)) {
                out.add(preBuffer.readableBytesArray());
                preBuffer.clear();
                /*跳过一个\n读取下一个字符*/
                index = index + 2;
            } else {
                preBuffer.writeByte(data);
                /*读取下一个字符*/
                index++;
            }
        }
        out.add(preBuffer.readableBytesArray());
        return out;
    }

    /**
     * 判断字节数组某位置是否将要换行
     *
     * 13即\r
     * 10即\n
     * 两者的组合即换行
     *
     * @param bytes
     * @param index
     * @return 将要换行，返回ture，否则返回false
     */
    public static boolean isNextLine(byte[] bytes, int index) {
        if (bytes.length < index + 1) {
            return false;
        }
        return bytes[index] == 13 && bytes[index + 1] == 10;
    }
}
