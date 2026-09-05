package com.room209.backend.dto;

public class AuthResponse {
    private String token;
    private UserDto user;
    private Long roomId;
    private String roomNumber;

    public AuthResponse() {}

    public AuthResponse(String token, UserDto user, Long roomId, String roomNumber) {
        this.token = token;
        this.user = user;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
}
