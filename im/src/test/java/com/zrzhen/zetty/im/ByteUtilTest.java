package com.zrzhen.zetty.im;

import com.zrzhen.zetty.im.util.ByteUtil;
import org.junit.Test;

import java.nio.ByteBuffer;

import static junit.framework.TestCase.assertEquals;

public class ByteUtilTest {


    @Test
    public void msgLength() {
        ByteBuffer byteBuffer = ByteUtil.msgEncode("123456789");
        byteBuffer.flip();
        int length = ByteUtil.msgLength(byteBuffer);
        assertEquals(9,length);
    }

    @Test
    public void msgDecode() {
        String data = "中国对对对";
        ByteBuffer byteBuffer = ByteUtil.msgEncode(data);
        byteBuffer.flip();
        String actual =ByteUtil.msgDecode(byteBuffer);
        assertEquals(data, actual);

    }

    @Test
    public void int2byte() {
        int testNum = 100;
        byte[] int_byte = ByteUtil.int2byte(testNum);
        int byte_int = ByteUtil.byte2int(int_byte);
        assertEquals(testNum, byte_int);
    }
}
