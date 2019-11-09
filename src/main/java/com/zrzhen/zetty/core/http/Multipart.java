package com.zrzhen.zetty.core.http;

/**
 * @author chenanlian
 */
public class Multipart {

    private String filename;
    private byte[] body;

    public Multipart(String filename, byte[] body) {
        this.filename = filename;
        this.body = body;
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
}
