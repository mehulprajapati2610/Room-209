package com.room209.backend.controller;

import com.room209.backend.dto.ChoreCreateRequest;
import com.room209.backend.dto.ChoreDto;
import com.room209.backend.service.ChoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/chores")
public class ChoreController {

    private final ChoreService choreService;

    public ChoreController(ChoreService choreService) {
        this.choreService = choreService;
    }

    @GetMapping
    public ResponseEntity<List<ChoreDto>> getChores(@PathVariable Long roomId) {
        return ResponseEntity.ok(choreService.getChores(roomId));
    }

    @PostMapping
    public ResponseEntity<ChoreDto> createChore(
            @PathVariable Long roomId,
            @RequestBody ChoreCreateRequest request) {
        return ResponseEntity.ok(choreService.createChore(roomId, request));
    }

    @PatchMapping("/{choreId}/toggle")
    public ResponseEntity<ChoreDto> toggleChoreCompletion(
            @PathVariable Long roomId,
            @PathVariable Long choreId) {
        return ResponseEntity.ok(choreService.toggleChoreCompletion(choreId));
    }
}
