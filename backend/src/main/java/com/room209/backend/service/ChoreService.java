package com.room209.backend.service;

import com.room209.backend.dto.ChoreCreateRequest;
import com.room209.backend.dto.ChoreDto;
import com.room209.backend.entity.Chore;
import com.room209.backend.entity.Room;
import com.room209.backend.entity.User;
import com.room209.backend.repository.ChoreRepository;
import com.room209.backend.repository.RoomRepository;
import com.room209.backend.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChoreService {

    private final ChoreRepository choreRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RealtimePublisher realtimePublisher;

    public ChoreService(ChoreRepository choreRepository, RoomRepository roomRepository,
                        UserRepository userRepository, RealtimePublisher realtimePublisher) {
        this.choreRepository = choreRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.realtimePublisher = realtimePublisher;
    }

    @Transactional(readOnly = true)
    public List<ChoreDto> getChores(Long roomId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "completed").and(Sort.by(Sort.Direction.ASC, "dueDate"));
        return choreRepository.findByRoomId(roomId, sort).stream()
                .map(ChoreDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChoreDto createChore(Long roomId, ChoreCreateRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));

        User assignedTo = null;
        if (request.getAssignedToUserId() != null) {
            assignedTo = userRepository.findById(request.getAssignedToUserId()).orElse(null);
        }

        Chore chore = new Chore(
                room,
                request.getTitle(),
                request.getDescription(),
                assignedTo,
                request.getDueDate(),
                request.getCategory() != null ? request.getCategory() : "CLEANING"
        );

        Chore saved = choreRepository.save(chore);
        ChoreDto dto = new ChoreDto(saved);

        realtimePublisher.publishChoreEvent(roomId, "CHORE_CREATED", dto);
        return dto;
    }

    @Transactional
    public ChoreDto toggleChoreCompletion(Long choreId) {
        Chore chore = choreRepository.findById(choreId)
                .orElseThrow(() -> new RuntimeException("Chore not found: " + choreId));

        chore.setCompleted(!chore.isCompleted());
        Chore saved = choreRepository.save(chore);

        ChoreDto dto = new ChoreDto(saved);
        realtimePublisher.publishChoreEvent(chore.getRoom().getId(), "CHORE_UPDATED", dto);
        return dto;
    }
}
