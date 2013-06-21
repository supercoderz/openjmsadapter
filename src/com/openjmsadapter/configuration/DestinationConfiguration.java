package com.openjmsadapter.configuration;

public class DestinationConfiguration {

    private String destinationType;
    private String operationType;
    private String destinantionName;
    private long lastSequenceSent;
    private long lastSequenceReceived;

    public String getDestinantionName() {
        return destinantionName;
    }

    public void setDestinantionName(String destinantionName) {
        this.destinantionName = destinantionName;
    }

    public long getLastSequenceReceived() {
        return lastSequenceReceived;
    }

    public void setLastSequenceReceived(long lastSequenceReceived) {
        this.lastSequenceReceived = lastSequenceReceived;
    }

    public long getLastSequenceSent() {
        return lastSequenceSent;
    }

    public void setLastSequenceSent(long lastSequenceSent) {
        this.lastSequenceSent = lastSequenceSent;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
