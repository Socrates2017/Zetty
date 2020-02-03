package com.zrzhen.zetty.common;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class FileUtilTest {

    @Test
    public void str2ByteTest() {

        String data = "test 测试数据";
        byte[] bytes = FileUtil.str2Byte(data);
        String result = FileUtil.byte2Str(bytes);
        assertEquals(data, result);

    }
}
