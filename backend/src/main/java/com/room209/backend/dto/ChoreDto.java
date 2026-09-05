package com.room209.backend.dto;

import com.room209.backend.entity.Chore;
import java.time.LocalDate;

public class ChoreDto {
    private Long id;
    private String title;
    private String description;
    private UserDto assignedTo;
    private LocalDate dueDate;
    private boolean completed;
    private String category;

    public ChoreDto() {}

    public ChoreDto(Chore chore) {
        this.id = chore.getId();
        this.title = chore.getTitle();
        this.description = chore.getDescription();
        if (chore.getAssignedTo() != null) {
            this.assignedTo = new UserDto(chore.getAssignedTo());
        }
        this.dueDate = chore.getDueDate();
        this.completed = chore.isCompleted();
        this.category = chore.getCategory();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public UserDto getAssignedTo() { return assignedTo; }
    public void setAssignedTo(UserDto assignedTo) { this.assignedTo = assignedTo; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
