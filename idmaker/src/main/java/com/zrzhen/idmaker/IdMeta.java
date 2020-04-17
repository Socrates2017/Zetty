package com.zrzhen.idmaker;

public class IdMeta {

    /** 开始时间截 (2015-01-01) */
    private final long epoch = 1587141600303L;

    /** 数据标识id所占的位数 */
    private final long datacenterIdBits = 9L;

    /** 机器id所占的位数 */
    private final long versionBits = 1L;

    /** 序列在id中占的位数 */
    private final long sequenceBits = 12L;

    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxVersion = -1L ^ (-1L << versionBits);

    /** 支持的最大数据标识id，结果是31 */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /** 机器ID向左移12位 */
    private final long versionShift = sequenceBits;

    /** 数据标识id向左移17位(12+5) */
    private final long datacenterIdShift = sequenceBits + versionBits;

    /** 时间截向左移22位(5+5+12) */
    private final long timestampLeftShift = sequenceBits + versionBits + datacenterIdBits;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);


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


}
