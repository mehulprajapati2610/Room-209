package com.room209.backend.controller;

import com.room209.backend.dto.PresenceUpdateRequest;
import com.room209.backend.dto.RoomDto;
import com.room209.backend.dto.UserDto;
import com.room209.backend.entity.User;
import com.room209.backend.service.AuthService;
import com.room209.backend.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final AuthService authService;

    public RoomController(RoomService roomService, AuthService authService) {
        this.roomService = roomService;
        this.authService = authService;
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomDetails(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomDetails(roomId));
    }

    @GetMapping("/number/{roomNumber}")
    public ResponseEntity<RoomDto> getRoomByNumber(@PathVariable String roomNumber) {
        return ResponseEntity.ok(roomService.getRoomByNumber(roomNumber));
    }

    @GetMapping("/{roomId}/roommates")
    public ResponseEntity<List<UserDto>> getRoommates(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoommates(roomId));
    }

    @PutMapping("/{roomId}/presence")
    public ResponseEntity<UserDto> updatePresence(@PathVariable Long roomId,
                                                  @RequestBody PresenceUpdateRequest request) {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(roomService.updatePresence(roomId, currentUser.getId(), request.getStatus()));
    }

    @PutMapping("/{roomId}/quiet-hours")
    public ResponseEntity<RoomDto> updateQuietHours(@PathVariable Long roomId,
                                                    @RequestBody Map<String, Object> body) {
        LocalTime start = LocalTime.parse((String) body.get("start"));
        LocalTime end = LocalTime.parse((String) body.get("end"));
        boolean enabled = body.get("enabled") != null ? (Boolean) body.get("enabled") : true;

        return ResponseEntity.ok(roomService.updateQuietHours(roomId, start, end, enabled));
    }

    @PutMapping("/fcm-token")
    public ResponseEntity<Void> updateFcmToken(@RequestBody Map<String, String> body) {
        User currentUser = authService.getCurrentUser();
        String token = body.get("token");
        roomService.updateFcmToken(currentUser.getId(), token);
        return ResponseEntity.ok().build();
    }

    // Direct STOMP message handler for presence
    @MessageMapping("/room.{roomId}.presence.update")
    public void handleWsPresenceUpdate(@DestinationVariable Long roomId, PresenceUpdateRequest request) {
        User currentUser = authService.getCurrentUser();
        roomService.updatePresence(roomId, currentUser.getId(), request.getStatus());
    }
}
