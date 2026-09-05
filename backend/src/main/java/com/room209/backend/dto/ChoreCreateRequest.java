package com.room209.backend.dto;

import java.time.LocalDate;

public class ChoreCreateRequest {
    private String title;
    private String description;
    private Long assignedToUserId;
    private LocalDate dueDate;
    private String category; // "CLEANING", "BATHROOM", "TRASH", "KITCHEN"

    public ChoreCreateRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getAssignedToUserId() { return assignedToUserId; }
    public void setAssignedToUserId(Long assignedToUserId) { this.assignedToUserId = assignedToUserId; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
