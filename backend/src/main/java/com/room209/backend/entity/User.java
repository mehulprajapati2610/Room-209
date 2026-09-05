package com.room209.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String bedNumber; // "Bed 1", "Bed 2", etc.
    private String roomRole;  // "Room Lead", "Resident"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PresenceStatus presenceStatus = PresenceStatus.IN_ROOM;

    private String avatarUrl;
    private String fcmToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

    public enum PresenceStatus {
        IN_ROOM,
        AWAY,
        STUDYING_QUIET,
        DO_NOT_DISTURB
    }

    public User() {}

    public User(String email, String password, String name, String bedNumber, String roomRole, Room room) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.bedNumber = bedNumber;
        this.roomRole = roomRole;
        this.room = room;
        this.presenceStatus = PresenceStatus.IN_ROOM;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBedNumber() { return bedNumber; }
    public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }

    public String getRoomRole() { return roomRole; }
    public void setRoomRole(String roomRole) { this.roomRole = roomRole; }

    public PresenceStatus getPresenceStatus() { return presenceStatus; }
    public void setPresenceStatus(PresenceStatus presenceStatus) { this.presenceStatus = presenceStatus; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
}
