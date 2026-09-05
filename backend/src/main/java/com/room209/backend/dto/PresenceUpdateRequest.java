package com.room209.backend.dto;

import com.room209.backend.entity.User;

public class PresenceUpdateRequest {
    private User.PresenceStatus status;

    public PresenceUpdateRequest() {}
    public PresenceUpdateRequest(User.PresenceStatus status) { this.status = status; }

    public User.PresenceStatus getStatus() { return status; }
    public void setStatus(User.PresenceStatus status) { this.status = status; }
}
