package com.zrzhen.idmaker.core;

public class IdMeta {

    /**
     * 机器位
     */
    private byte machineBits;

    /**
     * 序列号
     */
    private byte seqBits;

    /**
     * 时间戳位
     */
    private byte timeBits;

    /**
     * 生成方式
     */
    private byte genMethodBits;

    /**
     * 类型
     */
    private byte typeBits;

    /**
     * 版本
     */
    private byte versionBits;

    public IdMeta(byte machineBits, byte seqBits, byte timeBits, byte genMethodBits, byte typeBits, byte versionBits) {
        super();

        this.machineBits = machineBits;
        this.seqBits = seqBits;
        this.timeBits = timeBits;
        this.genMethodBits = genMethodBits;
        this.typeBits = typeBits;
        this.versionBits = versionBits;
    }

    public byte getMachineBits() {
        return machineBits;
    }

    public void setMachineBits(byte machineBits) {
        this.machineBits = machineBits;
    }

    public long getMachineBitsMask() {
        return -1L ^ -1L << machineBits;
    }

    public byte getSeqBits() {
        return seqBits;
    }

    public void setSeqBits(byte seqBits) {
        this.seqBits = seqBits;
    }

    public long getSeqBitsStartPos() {
        return machineBits;
    }

    public long getSeqBitsMask() {
        return -1L ^ -1L << seqBits;
    }

    public byte getTimeBits() {
        return timeBits;
    }

    public void setTimeBits(byte timeBits) {
        this.timeBits = timeBits;
    }

    public long getTimeBitsStartPos() {
        return machineBits + seqBits;
    }

    public long getTimeBitsMask() {
        return -1L ^ -1L << timeBits;
    }

    public byte getGenMethodBits() {
        return genMethodBits;
    }

    public void setGenMethodBits(byte genMethodBits) {
        this.genMethodBits = genMethodBits;
    }

    public long getGenMethodBitsStartPos() {
        return machineBits + seqBits + timeBits;
    }

    public long getGenMethodBitsMask() {
        return -1L ^ -1L << genMethodBits;
    }

    public byte getTypeBits() {
        return typeBits;
    }

    public void setTypeBits(byte typeBits) {
        this.typeBits = typeBits;
    }

    public long getTypeBitsStartPos() {
        return machineBits + seqBits + timeBits + genMethodBits;
    }

    public long getTypeBitsMask() {
        return -1L ^ -1L << typeBits;
    }

    public byte getVersionBits() {
        return versionBits;
    }

    public void setVersionBits(byte versionBits) {
        this.versionBits = versionBits;
    }

    public long getVersionBitsStartPos() {
        return machineBits + seqBits + timeBits + genMethodBits + typeBits;
    }

    public long getVersionBitsMask() {
        return -1L ^ -1L << versionBits;
    }
}
