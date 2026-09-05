package com.room209.backend.controller;

import com.room209.backend.dto.PlanCreateRequest;
import com.room209.backend.dto.PlanDto;
import com.room209.backend.entity.PlanRsvp;
import com.room209.backend.entity.User;
import com.room209.backend.service.AuthService;
import com.room209.backend.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/plans")
public class PlanController {

    private final PlanService planService;
    private final AuthService authService;

    public PlanController(PlanService planService, AuthService authService) {
        this.planService = planService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<PlanDto>> getPlans(@PathVariable Long roomId) {
        return ResponseEntity.ok(planService.getPlans(roomId));
    }

    @PostMapping
    public ResponseEntity<PlanDto> createPlan(
            @PathVariable Long roomId,
            @RequestBody PlanCreateRequest request) {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(planService.createPlan(roomId, currentUser, request));
    }

    @PutMapping("/{planId}/rsvp")
    public ResponseEntity<PlanDto> updateRsvp(
            @PathVariable Long roomId,
            @PathVariable Long planId,
            @RequestBody Map<String, String> body) {
        User currentUser = authService.getCurrentUser();
        PlanRsvp.RsvpStatus status = PlanRsvp.RsvpStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(planService.updateRsvp(planId, currentUser, status));
    }
}
