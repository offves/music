package com.offves.stream.event;

public abstract class Event {

    private final long timestamp;

    public Event() {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return this.timestamp;
    }

}
