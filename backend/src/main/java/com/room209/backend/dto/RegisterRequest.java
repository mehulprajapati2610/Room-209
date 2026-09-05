package com.room209.backend.dto;

public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String bedNumber; // "Bed 1", "Bed 2"
    private String roomRole;  // "Resident", "Room Lead"

    public RegisterRequest() {}

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
}
