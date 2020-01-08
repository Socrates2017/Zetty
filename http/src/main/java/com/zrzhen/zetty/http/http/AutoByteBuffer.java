package com.zrzhen.zetty.http.http;

import com.zrzhen.zetty.http.http.bufpool.AutoByteBufferPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 描述：数组缓存处理类，用于解决需要写入一个未知长度的byte[]，如果使用定长数组的话，处理起来相对麻烦，
 * 所以引入一个可自动扩容的数组类。
 *
 * @author chenanlian
 */
public class AutoByteBuffer {

    private static Logger log = LoggerFactory.getLogger(AutoByteBuffer.class);

    /**
     * 默认容量
     */
    private static final int BUFFER_SIZE = Constant.BUFFER_SIZE;
    /**
     * 指针位置，即将读取的位置
     */
    private int readerIndex;
    /**
     * 指针位置，即将写入的位置
     */
    private int writerIndex;

    /**
     * 已储存未处理的数组
     */
    private byte[] data;

    /**
     * 私有化构造函数，获取实例可以使用静态方法
     *
     * @param capacity 容量
     */
    private AutoByteBuffer(int capacity) {
        data = new byte[capacity];
    }

    /**
     * 获取一个新的实例，从对象池获取
     *
     * @return 默认容量的实例
     */
    public static AutoByteBuffer newByteBuffer() {
        try {
            return AutoByteBufferPoolUtil.bufferPool.borrowObject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return newByteBuffer2();
        }
    }

    /**
     * 获取一个新实例，直接生成
     *
     * @return 默认容量的实例
     */
    public static AutoByteBuffer newByteBuffer2() {
        return new AutoByteBuffer(BUFFER_SIZE);
    }

    /**
     * 获取一个新的实例，直接生成
     *
     * @param capacity 容量
     * @return AutoByteBuffer实例
     */
    public static AutoByteBuffer newByteBuffer(int capacity) {
        return new AutoByteBuffer(capacity);
    }

    /**
     * 获取底层数组，包含未写入数据的部分
     *
     * @return 数据
     */
    public byte[] array() {
        return data;
    }

    /**
     * 获取剩余可读数据
     *
     * @return 剩余可读数据
     */
    public byte[] readableBytesArray() {
        byte[] bytes = new byte[readableBytes()];
        readBytesFromBytes(data, bytes, readerIndex);
        return bytes;
    }

    /**
     * 重置读取指针位置,如果大于写入位置，则可读位置重置为写入位置，readableBytes()结果则为0
     *
     * @param position 位置
     * @return 调用者自己
     */
    public AutoByteBuffer readerIndex(int position) {
        if (position <= writerIndex) {
            readerIndex = position;
        } else {
            readerIndex = writerIndex;
        }
        return this;
    }


    /**
     * 清理数据，重置指针，数据不保留
     *
     * @return
     */
    public AutoByteBuffer clear() {
        readerIndex = 0;
        writerIndex = 0;
        data = new byte[BUFFER_SIZE];
        return this;
    }

    /**
     * 清理指针标记，数组内容保留，但是会被覆盖，除了array()获取原始数组外无法得到旧数据
     *
     * @return
     */
    public AutoByteBuffer reset() {
        readerIndex = 0;
        writerIndex = 0;
        return this;
    }

    public boolean returnPool() {
        try {
            AutoByteBufferPoolUtil.bufferPool.returnObject(this);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }


    /**
     * 获取所有写入的数据
     *
     * @return
     */
    public byte[] readAllWriteBytesArray() {
        byte[] bytes = new byte[writerIndex];
        readBytesFromBytes(data, bytes, 0);
        return bytes;
    }

    /**
     * 删除已读部分，重新初始化数组
     */
    public void discardReadBytes() {
        byte[] newBytes = new byte[capacity()];
        int oldReadableBytes = readableBytes();
        //参数意思：原数组，源数组要复制的起始位置，目标数组，目标数组起始位置，要复制的长度
        System.arraycopy(data, readerIndex, newBytes, 0, oldReadableBytes);
        writerIndex = oldReadableBytes;
        readerIndex = 0;
        data = newBytes;
    }

    /**
     * 读取指针位置
     *
     * @return
     */
    public int readerIndex() {
        return readerIndex;
    }

    /**
     * 获取写入指针位置
     *
     * @return
     */
    public int writerIndex() {
        return writerIndex;
    }

    /**
     * 当前可读数据量，即已写入的数据减去已读的数据的量：writerIndex - readerIndex
     *
     * @return
     */
    public int readableBytes() {
        return writerIndex - readerIndex;
    }


    /**
     * 当前是否有可读
     *
     * @return boolean
     * @params []
     */
    public boolean hasRemaining() {
        if ((writerIndex - readerIndex) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 当前可写入数据量，每次触发扩容后都不一样
     *
     * @return
     */
    public int writableBytes() {
        return data.length - writerIndex;
    }


    /**
     * 当前容量，当写入数据超过当前容量后自动扩容
     *
     * @return
     */
    public int capacity() {
        return data.length;
    }

    /**
     * 读取一个数据到byte，从readIndex位置开始，每读取一个，指针+1
     *
     * @return
     * @throws ByteBufferException
     */
    public int read() throws ByteBufferException {
        if (readableBytes() > 0) {
            int i = data[readerIndex];
            readerIndex++;
            return i;
        } else {
            throw new ByteBufferException("readableBytes = 0");
        }
    }

    /**
     * 读取数据到byte，1 byte，从readIndex位置开始
     *
     * @return
     * @throws ByteBufferException
     */
    public byte readByte() throws ByteBufferException {
        if (readableBytes() > 0) {
            byte i = data[readerIndex];
            readerIndex++;
            return i;
        } else {
            throw new ByteBufferException("readableBytes = 0");
        }
    }

    /**
     * 读取integer值，读4 byte转换为integer，从readIndex位置开始
     *
     * @return
     * @throws ByteBufferException
     */
    public int readInt() throws ByteBufferException {
        if (readableBytes() >= 4) {
            int result = byteArrayToInt(data, readerIndex);
            readerIndex += 4;
            return result;
        } else {
            throw new ByteBufferException("readableBytes < 4");
        }
    }

    /**
     * 读取数据到bytes，从readIndex位置开始
     *
     * @param bytes
     * @return
     * @throws ByteBufferException
     */
    public int readBytes(byte[] bytes) throws ByteBufferException {
        if (readableBytes() >= bytes.length) {
            int result = readBytesFromBytes(data, bytes, readerIndex);
            readerIndex += bytes.length;
            return result;
        } else {
            throw new ByteBufferException("readableBytes < " + bytes.length);
        }
    }

    /**
     * 读取数据到另一个ByteBuffer
     *
     * @param b
     * @return
     */
    public int readBytes(AutoByteBuffer b) {
        byte[] bytes = new byte[b.writableBytes()];
        int result = readBytesFromBytes(data, bytes, readerIndex);
        readerIndex += bytes.length;
        b.writeBytes(bytes);
        return result;

    }

    /**
     * 写入Byte数据，1 byte
     *
     * @param b
     * @return
     */
    public AutoByteBuffer writeByte(byte b) {
        autoExpandCapacity(1);
        data[writerIndex] = b;
        writerIndex++;
        return this;

    }

    /**
     * 写入int值的byte转换结果，即丢弃高位
     *
     * @param b
     * @return
     */
    public AutoByteBuffer write(int b) {
        autoExpandCapacity(1);
        data[writerIndex] = (byte) b;
        writerIndex++;
        return this;

    }

    /**
     * 写入integer数据，4 byte
     *
     * @param b
     * @return
     */
    public AutoByteBuffer writeInt(int b) {
        autoExpandCapacity(4);
        writeBytesToBytes(intToByteArray(b), data, writerIndex);
        writerIndex += 4;
        return this;

    }

    /**
     * 写入数组
     *
     * @param b
     * @return
     */
    public AutoByteBuffer writeBytes(byte[] b) {
        autoExpandCapacity(b.length);
        writeBytesToBytes(b, data, writerIndex);
        writerIndex += b.length;
        return this;
    }

    /**
     * 写入数组,并指定写入长度
     *
     * @param b
     * @return
     */
    public AutoByteBuffer writeBytes(byte[] b, int dataLength) {
        autoExpandCapacity(b.length);
        writeBytesToBytes(b, data, writerIndex, dataLength);
        writerIndex += dataLength;
        return this;
    }

    public AutoByteBuffer writeBytes(byte[] b, int srcPos, int dataLength) {
        autoExpandCapacity(b.length);
        writeBytesToBytes(b, srcPos, data, writerIndex, dataLength);
        writerIndex += dataLength;
        return this;
    }

    /**
     * 写入一个ByteBuffer可读数据
     *
     * @param b
     * @return
     */
    public AutoByteBuffer writeBytes(AutoByteBuffer b) {
        int readableBytes = b.readableBytes();
        autoExpandCapacity(readableBytes);
        writeBytesToBytes(b.readableBytesArray(), data, writerIndex);
        b.readerIndex(b.writerIndex);
        writerIndex += readableBytes;
        return this;
    }

    /**
     * 写入一个ByteBuffer可读数据的部分长度
     *
     * @param b
     * @param dataLength
     * @return
     */
    public AutoByteBuffer writeBytes(AutoByteBuffer b, int dataLength) {
        autoExpandCapacity(dataLength);
        writeBytesToBytes(b.readableBytesArray(), data, writerIndex, dataLength);
        b.readerIndex(b.readerIndex + dataLength);
        writerIndex += dataLength;
        return this;
    }

    /**
     * 检查写入数据长度，如果不够则扩容,自动扩容,递增值为BUFFER_SIZE的倍数
     *
     * @param addLength
     */
    private void autoExpandCapacity(int addLength) {
        if (writableBytes() < addLength) {
            int newSize = writerIndex + addLength;
            int size = 0;
            while (size < newSize) {
                size += BUFFER_SIZE;
            }
            byte[] newBytes = new byte[size];
            writeBytesToBytes(data, newBytes, 0);
            data = newBytes;
        }
    }

    /**
     * 数组转换成整数型
     *
     * @param b
     * @return return 0；
     */
    private int byteArrayToInt(byte[] b, int position) {
        return b[position + 3] & 0xFF | (b[position + 2] & 0xFF) << 8 | (b[position + 1] & 0xFF) << 16 | (b[position] & 0xFF) << 24;
    }

    /**
     * 数据读取，从一个数组中读取一部分数组
     *
     * @param src
     * @param result
     * @param position
     * @return
     */
    private int readBytesFromBytes(byte[] src, byte[] result, int position) {
        System.arraycopy(src, position, result, 0, result.length);
        return src.length;
    }

    /**
     * 数组复制，向一个数组写入一个数组数组
     *
     * @param src            来源数组
     * @param target         被写入新数据数组
     * @param targetPosition 新数组被写入位置
     * @return
     */
    private AutoByteBuffer writeBytesToBytes(byte[] src, byte[] target, int targetPosition) {
        return writeBytesToBytes(src, target, targetPosition, src.length);
    }

    /**
     * 数组复制，向一个数组写入一个数组数组
     *
     * @param src            来源数组
     * @param target         被写入新数据数组
     * @param targetPosition 新数组被写入位置
     * @return
     */
    private AutoByteBuffer writeBytesToBytes(byte[] src, byte[] target, int targetPosition, int dataLength) {
        System.arraycopy(src, 0, target, targetPosition, dataLength);
        return this;
    }

    /**
     * 数组复制，向一个数组写入一个数组数组
     *
     * @param src            来源数组
     * @param target         被写入新数据数组
     * @param targetPosition 新数组被写入位置
     * @return
     */
    private AutoByteBuffer writeBytesToBytes(byte[] src, int srcPos, byte[] target, int targetPosition, int dataLength) {
        System.arraycopy(src, srcPos, target, targetPosition, dataLength);
        return this;
    }

    /**
     * 整数转换成数组
     *
     * @param i
     * @return byte length=4
     */
    private byte[] intToByteArray(int i) {
        return new byte[]{(byte) ((i >> 24) & 0xFF), (byte) ((i >> 16) & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) (i & 0xFF)};
    }

    public static class ByteBufferException extends IOException {
        ByteBufferException(String message) {
            super(message);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ByteBuffer{");
        sb.append("readerIndex=").append(readerIndex);
        sb.append(", writerIndex=").append(writerIndex);
        sb.append(", capacity=").append(data.length);
        sb.append('}');
        return sb.toString();
    }

}

