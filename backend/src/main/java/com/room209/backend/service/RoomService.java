package com.room209.backend.service;

import com.room209.backend.dto.RoomDto;
import com.room209.backend.dto.UserDto;
import com.room209.backend.entity.Room;
import com.room209.backend.entity.User;
import com.room209.backend.repository.RoomRepository;
import com.room209.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RealtimePublisher realtimePublisher;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository,
                       RealtimePublisher realtimePublisher) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.realtimePublisher = realtimePublisher;
    }

    @Transactional(readOnly = true)
    public RoomDto getRoomDetails(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));
        return new RoomDto(room);
    }

    @Transactional(readOnly = true)
    public RoomDto getRoomByNumber(String roomNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));
        return new RoomDto(room);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getRoommates(Long roomId) {
        return userRepository.findByRoomId(roomId).stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto updatePresence(Long roomId, Long userId, User.PresenceStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        user.setPresenceStatus(status);
        userRepository.save(user);

        UserDto userDto = new UserDto(user);
        realtimePublisher.publishPresenceUpdate(roomId, userDto);
        return userDto;
    }

    @Transactional
    public RoomDto updateQuietHours(Long roomId, LocalTime start, LocalTime end, boolean enabled) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));

        room.setQuietHoursStart(start);
        room.setQuietHoursEnd(end);
        room.setQuietHoursEnabled(enabled);
        roomRepository.save(room);

        RoomDto dto = new RoomDto(room);
        realtimePublisher.publishPresenceUpdate(roomId, dto);
        return dto;
    }

    @Transactional
    public void updateFcmToken(Long userId, String token) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setFcmToken(token);
            userRepository.save(user);
        });
    }
}
