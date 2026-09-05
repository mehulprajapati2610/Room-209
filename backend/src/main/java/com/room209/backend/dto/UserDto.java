package com.room209.backend.dto;

import com.room209.backend.entity.User;

public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String bedNumber;
    private String roomRole;
    private User.PresenceStatus presenceStatus;
    private String avatarUrl;

    public UserDto() {}

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.bedNumber = user.getBedNumber();
        this.roomRole = user.getRoomRole();
        this.presenceStatus = user.getPresenceStatus();
        this.avatarUrl = user.getAvatarUrl();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBedNumber() { return bedNumber; }
    public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }

    public String getRoomRole() { return roomRole; }
    public void setRoomRole(String roomRole) { this.roomRole = roomRole; }

    public User.PresenceStatus getPresenceStatus() { return presenceStatus; }
    public void setPresenceStatus(User.PresenceStatus presenceStatus) { this.presenceStatus = presenceStatus; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}
