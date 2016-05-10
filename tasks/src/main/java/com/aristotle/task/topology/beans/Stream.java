package com.aristotle.task.topology.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ravi Sharma on 01/07/2015.
 */
public class Stream implements Serializable {

    private static final long serialVersionUID = 1L;

    private String streamId;
    private List<String> fields;


    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }


}
