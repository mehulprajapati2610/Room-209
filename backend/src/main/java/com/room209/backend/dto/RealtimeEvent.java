package com.room209.backend.dto;

import java.time.LocalDateTime;

public class RealtimeEvent {
    private String type; // "PRESENCE", "FEED", "PLAN", "CHORE", "POLL"
    private String action; // "CREATED", "UPDATED", "DELETED"
    private Object payload;
    private LocalDateTime timestamp = LocalDateTime.now();

    public RealtimeEvent() {}

    public RealtimeEvent(String type, String action, Object payload) {
        this.type = type;
        this.action = action;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Object getPayload() { return payload; }
    public void setPayload(Object payload) { this.payload = payload; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
