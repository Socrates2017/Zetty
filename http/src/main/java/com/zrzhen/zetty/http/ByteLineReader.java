package com.zrzhen.zetty.http;

import com.zrzhen.zetty.http.util.ByteUtil;

import java.util.List;

/**
 * @author chenanlian
 *
 * 将字节数组按分行读取，提供可逐行读取的方法
 */
public class ByteLineReader {

    private int readIndex;

    private int lineNum;

    private List<byte[]> bytesLines;

    public ByteLineReader(byte[] bytes) {
        bytesLines = ByteUtil.readBytesByLine(bytes);
        readIndex=0;
        lineNum= bytesLines.size();
    }

    public int getReadIndex() {
        return readIndex;
    }

    public int getLineNum() {
        return lineNum;
    }

    public byte[] nextLine() {
        if (readIndex + 1 >= lineNum) {
            return null;
        }
        readIndex++;
        return bytesLines.get(readIndex);
    }

    public List<byte[]> getBytesLines() {
        return bytesLines;
    }
}
