package com.zrzhen.zetty.http.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author chenanlian
 * <p>
 * 分行读取磁盘文件，避免一次性加载一个大文件进内存导致内存溢出
 */
public class ByteLineHardReader {

    private static Logger log = LoggerFactory.getLogger(ByteLineHardReader.class);

    /**
     * 文件路径
     */
    private String path;

    /**
     * 读取到的位置
     */
    private int readIndex;

    /**
     * 每次通道读取的大小
     */
    private int size;

    /**
     * 读到的一行数据
     */
    private byte[] line;

    private boolean readEnd;

    public ByteLineHardReader(String path) {
        this.path = path;
        this.size = 1024 ;
    }

    /**
     * 读取下一行
     *
     * @return
     */
    public byte[] nextPart() {
        line = null;
        if (readEnd) {
            return line;
        }

        AutoByteBuffer preBuffer = AutoByteBuffer.newByteBuffer();
        File f = new File(path);

        MappedByteBuffer inputBuffer = null;
        FileChannel fileChannel = null;
        try {
            int readSize;
            if (f.length() - readIndex - 1 >= size) {
                readSize = size;
            } else {
                readSize = (int) f.length() - readIndex;
                readEnd = true;
            }
            fileChannel = new RandomAccessFile(f, "r").getChannel();
            inputBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, readIndex, readSize);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (fileChannel.isOpen()) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        for (int i = 0; i < inputBuffer.capacity() - 1; ) {
            byte data = inputBuffer.get(i);
            preBuffer.writeByte(data);
            /*读取下一个字符*/
            i++;
            readIndex++;
        }

        line = preBuffer.readableBytesArray();
        preBuffer.reset();
        preBuffer.returnPool();
        return line;
    }

    /**
     * 读取下一行
     *
     * @return
     */
    public byte[] nextLine() {
        line = null;
        readHard();
        return line;
    }


    public void readHard() {

        /**
         * map(FileChannel.MapMode mode,long position, long size)
         * <p>
         * mode - 根据是按只读、读取/写入或专用（写入时拷贝）来映射文件，分别为 FileChannel.MapMode 类中所定义的
         * READ_ONLY、READ_WRITE 或 PRIVATE 之一
         * <p>
         * position - 文件中的位置，映射区域从此位置开始；必须为非负数
         * <p>
         * size - 要映射的区域大小；必须为非负数且不大于 Integer.MAX_VALUE
         * <p>
         * 所以若想读取文件后半部分内容，如例子所写；若想读取文本后1/8内容，需要这样写map(FileChannel.MapMode.READ_ONLY,
         * f.length()*7/8,f.length()/8)
         * <p>
         * 想读取文件所有内容，需要这样写map(FileChannel.MapMode.READ_ONLY, 0,f.length())
         */

        AutoByteBuffer preBuffer = AutoByteBuffer.newByteBuffer();
        File f = new File(path);
        do {
            MappedByteBuffer inputBuffer = null;
            FileChannel fileChannel = null;
            try {
                int readSize;
                if (f.length() - readIndex - 1 >= size) {
                    readSize = size;
                } else {
                    readSize = (int) f.length() - readIndex;
                }
                fileChannel = new RandomAccessFile(f, "r").getChannel();
                inputBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, readIndex, readSize);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                try {
                    if (fileChannel.isOpen()) {
                        fileChannel.close();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }

            for (int i = 0; i < inputBuffer.capacity() - 1; ) {
                byte data = inputBuffer.get(i);

                /*
                 * 如果遇到要换行，则返回
                 */
                if (isNextLine(data, inputBuffer.get(i + 1))) {
                    line = preBuffer.readableBytesArray();
                    preBuffer.reset();
                    preBuffer.returnPool();
                    break;
                } else {
                    preBuffer.writeByte(data);
                    /*读取下一个字符*/
                    i++;
                    readIndex++;
                }
            }
        } while (line == null && f.length() > readIndex);

        if (f.length() < readIndex) {
            line = null;
        }
    }


    /**
     * 是否即将换行
     *
     * @param byte1
     * @param byte2
     * @return
     */
    public boolean isNextLine(byte byte1, byte byte2) {
        if (byte1 == 13 && byte2 == 10) {
            readIndex = readIndex + 2;
            return true;
        }
        return false;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isReadEnd() {
        return readEnd;
    }

    public void setReadEnd(boolean readEnd) {
        this.readEnd = readEnd;
    }
}
