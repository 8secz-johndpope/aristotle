package com.aristotle.task.topology;

import java.io.Serializable;

public class MessageId<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private T data;
    private int retryCount = 0;
    private long startTime;
    private String streamId;

    public MessageId() {
        startTime = System.currentTimeMillis();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTimeSinceStart() {
        if (startTime == 0) {
            return 0L;
        }
        return System.currentTimeMillis() - startTime;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    @Override
    public String toString() {
        return "MessageId [data=" + data + ", retryCount=" + retryCount + ", startTime=" + startTime + "]";
    }

}
