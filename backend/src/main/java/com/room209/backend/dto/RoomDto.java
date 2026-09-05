package com.room209.backend.dto;

import com.room209.backend.entity.Room;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class RoomDto {
    private Long id;
    private String roomNumber;
    private String name;
    private LocalTime quietHoursStart;
    private LocalTime quietHoursEnd;
    private boolean quietHoursEnabled;
    private boolean isQuietHoursActiveNow;
    private List<UserDto> members;

    public RoomDto() {}

    public RoomDto(Room room) {
        this.id = room.getId();
        this.roomNumber = room.getRoomNumber();
        this.name = room.getName();
        this.quietHoursStart = room.getQuietHoursStart();
        this.quietHoursEnd = room.getQuietHoursEnd();
        this.quietHoursEnabled = room.isQuietHoursEnabled();
        
        // Calculate if quiet hours is active currently
        LocalTime now = LocalTime.now();
        if (room.isQuietHoursEnabled() && room.getQuietHoursStart() != null && room.getQuietHoursEnd() != null) {
            if (room.getQuietHoursStart().isAfter(room.getQuietHoursEnd())) {
                // e.g. 23:00 to 07:00
                this.isQuietHoursActiveNow = now.isAfter(room.getQuietHoursStart()) || now.isBefore(room.getQuietHoursEnd());
            } else {
                this.isQuietHoursActiveNow = now.isAfter(room.getQuietHoursStart()) && now.isBefore(room.getQuietHoursEnd());
            }
        } else {
            this.isQuietHoursActiveNow = false;
        }

        if (room.getMembers() != null) {
            this.members = room.getMembers().stream().map(UserDto::new).collect(Collectors.toList());
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalTime getQuietHoursStart() { return quietHoursStart; }
    public void setQuietHoursStart(LocalTime quietHoursStart) { this.quietHoursStart = quietHoursStart; }

    public LocalTime getQuietHoursEnd() { return quietHoursEnd; }
    public void setQuietHoursEnd(LocalTime quietHoursEnd) { this.quietHoursEnd = quietHoursEnd; }

    public boolean isQuietHoursEnabled() { return quietHoursEnabled; }
    public void setQuietHoursEnabled(boolean quietHoursEnabled) { this.quietHoursEnabled = quietHoursEnabled; }

    public boolean isQuietHoursActiveNow() { return isQuietHoursActiveNow; }
    public void setQuietHoursActiveNow(boolean quietHoursActiveNow) { isQuietHoursActiveNow = quietHoursActiveNow; }

    public List<UserDto> getMembers() { return members; }
    public void setMembers(List<UserDto> members) { this.members = members; }
}
