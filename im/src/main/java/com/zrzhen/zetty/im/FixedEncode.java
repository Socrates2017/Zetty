package com.zrzhen.zetty.im;

import com.zrzhen.zetty.im.util.ByteUtil;
import com.zrzhen.zetty.net.Encode;
import com.zrzhen.zetty.net.SocketSession;

import java.nio.ByteBuffer;

public class FixedEncode implements Encode<String> {

    @Override
    public ByteBuffer encode(SocketSession session, String out) {
        ByteBuffer writeBuffer = ByteUtil.msgEncode(out);
        writeBuffer.flip();
        return writeBuffer;
    }
}
