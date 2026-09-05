package com.room209.backend.dto;

import java.time.LocalDateTime;

public class PlanCreateRequest {
    private String title;
    private String description;
    private LocalDateTime scheduledTime;
    private String location;

    public PlanCreateRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
