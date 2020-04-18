package com.zrzhen.idmaker;

public class IdMeta {

    /**
     * 开始时间截
     */
    private long epoch = 1587000000000L;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 数据标识id所占的位数
     */
    private long datacenterIdBits = 9L;

    /**
     * 机器id所占的位数
     */
    private long versionBits = 1L;

    /**
     * 序列在id中占的位数
     */
    private long sequenceBits = 12L;


    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private long maxVersion = -1L ^ (-1L << versionBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /**
     * 机器ID向左移12位
     */
    private long versionShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private long datacenterIdShift = sequenceBits + versionBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private long timestampLeftShift = sequenceBits + versionBits + datacenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    public IdMeta() {
        init();
    }

    /**
     * @param epoch            开始时间戳
     * @param datacenterId     机器id
     * @param datacenterIdBits 机器id位数
     * @param versionBits      时间版本位数
     * @param sequenceBits     时间序列位数
     */
    public IdMeta(long epoch, long datacenterId, long datacenterIdBits, long versionBits, long sequenceBits) {
        this.epoch = epoch;
        this.datacenterId = datacenterId;
        this.datacenterIdBits = datacenterIdBits;
        this.versionBits = versionBits;
        this.sequenceBits = sequenceBits;
        init();
    }


    private void init() {
        /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
        maxVersion = -1L ^ (-1L << versionBits);

        /** 支持的最大数据标识id，结果是31 */
        maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

        /** 时间版本ID向左移12位 */
        versionShift = sequenceBits;

        /** 数据标识id向左移17位(12+5) */
        datacenterIdShift = sequenceBits + versionBits;

        /** 时间截向左移22位(5+5+12) */
        timestampLeftShift = sequenceBits + versionBits + datacenterIdBits;

        /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
        sequenceMask = -1L ^ (-1L << sequenceBits);
    }


    public long getVersionShift() {
        return versionShift;
    }

    public long getDatacenterIdShift() {
        return datacenterIdShift;
    }

    public long getTimestampLeftShift() {
        return timestampLeftShift;
    }

    public long getSequenceMask() {
        return sequenceMask;
    }

    public long getMaxVersion() {
        return maxVersion;
    }

    public long getMaxDatacenterId() {
        return maxDatacenterId;
    }

    public long getEpoch() {
        return epoch;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }

    public long getDatacenterIdBits() {
        return datacenterIdBits;
    }

    public long getVersionBits() {
        return versionBits;
    }

    public long getSequenceBits() {
        return sequenceBits;
    }
}
