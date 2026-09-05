package com.room209.backend.controller;

import com.room209.backend.dto.PollCreateRequest;
import com.room209.backend.dto.PollDto;
import com.room209.backend.entity.User;
import com.room209.backend.service.AuthService;
import com.room209.backend.service.PollService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms/{roomId}/polls")
public class PollController {

    private final PollService pollService;
    private final AuthService authService;

    public PollController(PollService pollService, AuthService authService) {
        this.pollService = pollService;
        this.authService = authService;
    }

    @GetMapping("/active")
    public ResponseEntity<PollDto> getActivePoll(@PathVariable Long roomId) {
        User currentUser = null;
        try {
            currentUser = authService.getCurrentUser();
        } catch (Exception ignored) {}
        return ResponseEntity.ok(pollService.getActivePoll(roomId, currentUser));
    }

    @PostMapping
    public ResponseEntity<PollDto> createPoll(
            @PathVariable Long roomId,
            @RequestBody PollCreateRequest request) {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(pollService.createPoll(roomId, currentUser, request));
    }

    @PostMapping("/{pollId}/options/{optionId}/vote")
    public ResponseEntity<PollDto> vote(
            @PathVariable Long roomId,
            @PathVariable Long pollId,
            @PathVariable Long optionId) {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(pollService.vote(pollId, optionId, currentUser));
    }
}
