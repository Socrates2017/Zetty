package com.zrzhen.idmaker;

public class Id {


    /**
     * 时间截
     */
    private long timestamp = -1L;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 时间回拨协调(0~1)
     */
    private long version = 0L;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }
}
