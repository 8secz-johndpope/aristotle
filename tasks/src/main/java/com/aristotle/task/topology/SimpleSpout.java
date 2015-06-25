package com.aristotle.task.topology;

import java.util.List;

import backtype.storm.tuple.Values;

public class SimpleSpout extends SpringAwareBaseSpout {

    private static final long serialVersionUID = 1L;
    

    @Override
    public void getNextTuple() {
        String message;
        try {
            System.out.println("Getting Next message");
            writeToStream(new Values("Hello World " + System.currentTimeMillis()));
            Thread.sleep(5000);
        } catch (Exception e) {
            logError("Unable to receive Location File message from AWS Quque", e);
        }
    }

    @Override
    protected MessageId<List<Object>> writeToStream(List<Object> tuple) {
        MessageId<List<Object>> messageId = new MessageId<>();
        messageId.setData(tuple);
        writeToStream(tuple, messageId);
        return messageId;
    }

    @Override
    protected void writeToStream(List<Object> tuple, Object messageId) {
        writeToStream(tuple, messageId, "as");
    }

    @Override
    protected String[] getFields() {
        return new String[] { "LocationSaveMessage" };
    }

    @Override
    public void onAck(Object msgId) {
        updateFileStatus(msgId, "Done", true);
    }

    @Override
    public void onFail(Object msgId) {
        updateFileStatus(msgId, "Fail", false);
    }

    private void updateFileStatus(Object msg, String status, boolean active) {
        System.out.println("status=" + status);

    }

}
