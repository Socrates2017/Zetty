package com.zrzhen.zetty.http.http.util;

import com.zrzhen.zetty.http.http.mvc.ContentTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author chenanlian
 */
public class FileUtil {

    private final static Logger log = LoggerFactory.getLogger(FileUtil.class);


    /**
     * 根据文件相对路径获取字节数组，文件位置在jar包内
     * <p>
     * 如：
     * byte[] data = FileUtil.file2ByteByRelativePath("static/img/favicon.ico");
     * 获取resources下面static/img/favicon.ico路径文件
     *
     * @param path
     * @return
     */
    public static byte[] file2ByteByRelativePath(String path) {

        try {
            /*去除开头的右斜线，以顺利读取文件*/
            while (path.startsWith("/")) {
                path = path.substring(1);
            }
            InputStream in = ClassLoader.getSystemResourceAsStream(path);
            if (null == in) {
                return null;
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[in.available()];
                int n = 0;
                while ((n = in.read(buffer)) != -1) {
                    out.write(buffer, 0, n);
                }
                return out.toByteArray();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据文件相对路径获取字符串文本，文件位置在jar包内
     * <p>
     * 如：
     * byte[] data = FileUtil.file2ByteByRelativePath("static/img/favicon.ico");
     * 获取resources下面static/img/favicon.ico路径文件
     *
     * @param path
     * @return
     */
    public static String file2StringByRelativePath(String path) {
        byte[] bytes = file2ByteByRelativePath(path);
        return new String(bytes);
    }


    /**
     * 根据文件路径（绝对路径）获取字节数组
     *
     * @param path
     * @return 字节数组
     */
    public static byte[] file2Byte(String path) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[in.available()];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    /**
     * 保存字节数组到磁盘上
     *
     * @param bytes 要保存的字节数组
     * @param path  保存的路径，绝对路径
     * @return
     */
    public static boolean byte2File(byte[] bytes, String path) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path, false);
            out.write(bytes);
            return true;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return false;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    log.error(e1.getMessage(), e1);
                }
            }
        }
    }

    /**
     * 保存字节数组到磁盘上
     *
     * @param bytes  要保存的字节数组
     * @param path   保存的路径，绝对路径
     * @param append 是否是增量模式
     * @return true：保存成功；false：保存失败
     */
    public static boolean byte2File(byte[] bytes, String path, boolean append) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path, append);
            out.write(bytes);
            return true;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return false;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    log.error(e1.getMessage(), e1);
                }
            }
        }
    }


    /**
     * @param source
     * @param dest
     * @throws IOException
     */
    public static boolean copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    /**
     * 通过文件的length() 方法获取文件大小，这个通常可以应用于本地文件的大小计算；
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        return file.length();
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
            if (StringUtils.isNotBlank(encoding)) {
                bytes = str.getBytes(encoding);
            } else {
                bytes = str.getBytes();
            }
            return bytes;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
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
            if (StringUtils.isNotBlank(encoding)) {
                out = new String(bytes, encoding);
            } else {
                out = new String(bytes);
            }
            return out;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
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

    /**
     * String 转换 ByteBuffer
     *
     * @param str
     * @return
     */
    public static ByteBuffer str2Buf(String str) {
        return ByteBuffer.wrap(str.getBytes());
    }

    /**
     * ByteBuffer 转换 String
     *
     * @param buffer
     * @return
     */
    public static String buf2Str(ByteBuffer buffer) {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;

        charset = Charset.forName("UTF-8");
        decoder = charset.newDecoder();
        try {
            buffer.flip();
            charBuffer = decoder.decode(buffer);
            buffer.clear();
            //charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();

        } catch (CharacterCodingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * TODO
     * 打开文件通道，nio
     *
     * @param path
     */
    private void openFileChannel(String path) {
        FileChannel fileChannel = null;
        try {
            fileChannel = new RandomAccessFile(path, "r").getChannel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int bytesRead = fileChannel.read(buf);

            if (bytesRead > 0) {
            }

        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (fileChannel != null) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    /**
     * 根据文件名来获得媒体类型
     *
     * @param fileName
     * @return
     */
    public static String contentTypeByFileName(String fileName) {
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
        if (StringUtils.isBlank(contentType)) {
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            contentType = ContentTypeEnum.getTypeBySuffix(suffix);
            if (StringUtils.isBlank(contentType)) {
                contentType = ContentTypeEnum.OCTET.getType();
            }
        }
        return contentType;
    }

}
