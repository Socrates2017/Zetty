package com.zrzhen.zetty.http.http;

/**
 * @author chenanlian
 */
public class Multipart {

    private String filename;
    private byte[] body;

    private String tmpPath;

    public Multipart(String filename, byte[] body) {
        this.filename = filename;
        this.body = body;
    }

    public Multipart(String filename, byte[] body, String tmpPath) {
        this.filename = filename;
        this.body = body;
        this.tmpPath = tmpPath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getTmpPath() {
        return tmpPath;
    }

    public void setTmpPath(String tmpPath) {
        this.tmpPath = tmpPath;
    }
}
