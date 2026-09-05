package com.room209.backend.dto;

import com.room209.backend.entity.Plan;
import com.room209.backend.entity.PlanRsvp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PlanDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime scheduledTime;
    private String location;
    private Plan.PlanStatus status;
    private UserDto createdBy;
    private List<RsvpDto> rsvps;

    public static class RsvpDto {
        private Long userId;
        private String userName;
        private PlanRsvp.RsvpStatus status;

        public RsvpDto() {}
        public RsvpDto(PlanRsvp rsvp) {
            this.userId = rsvp.getUser().getId();
            this.userName = rsvp.getUser().getName();
            this.status = rsvp.getRsvpStatus();
        }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }

        public PlanRsvp.RsvpStatus getStatus() { return status; }
        public void setStatus(PlanRsvp.RsvpStatus status) { this.status = status; }
    }

    public PlanDto() {}

    public PlanDto(Plan plan) {
        this.id = plan.getId();
        this.title = plan.getTitle();
        this.description = plan.getDescription();
        this.scheduledTime = plan.getScheduledTime();
        this.location = plan.getLocation();
        this.status = plan.getStatus();
        this.createdBy = new UserDto(plan.getCreatedBy());
        if (plan.getRsvps() != null) {
            this.rsvps = plan.getRsvps().stream().map(RsvpDto::new).collect(Collectors.toList());
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Plan.PlanStatus getStatus() { return status; }
    public void setStatus(Plan.PlanStatus status) { this.status = status; }

    public UserDto getCreatedBy() { return createdBy; }
    public void setCreatedBy(UserDto createdBy) { this.createdBy = createdBy; }

    public List<RsvpDto> getRsvps() { return rsvps; }
    public void setRsvps(List<RsvpDto> rsvps) { this.rsvps = rsvps; }
}
