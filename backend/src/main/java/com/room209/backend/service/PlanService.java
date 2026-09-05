package com.room209.backend.service;

import com.room209.backend.dto.PlanCreateRequest;
import com.room209.backend.dto.PlanDto;
import com.room209.backend.entity.Plan;
import com.room209.backend.entity.PlanRsvp;
import com.room209.backend.entity.Room;
import com.room209.backend.entity.User;
import com.room209.backend.repository.PlanRepository;
import com.room209.backend.repository.PlanRsvpRepository;
import com.room209.backend.repository.RoomRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanRsvpRepository planRsvpRepository;
    private final RoomRepository roomRepository;
    private final RealtimePublisher realtimePublisher;

    public PlanService(PlanRepository planRepository, PlanRsvpRepository planRsvpRepository,
                       RoomRepository roomRepository, RealtimePublisher realtimePublisher) {
        this.planRepository = planRepository;
        this.planRsvpRepository = planRsvpRepository;
        this.roomRepository = roomRepository;
        this.realtimePublisher = realtimePublisher;
    }

    @Transactional(readOnly = true)
    public List<PlanDto> getPlans(Long roomId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "scheduledTime");
        return planRepository.findByRoomId(roomId, sort).stream()
                .map(PlanDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlanDto createPlan(Long roomId, User creator, PlanCreateRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));

        Plan plan = new Plan(
                room,
                request.getTitle(),
                request.getDescription(),
                request.getScheduledTime(),
                request.getLocation(),
                creator
        );

        Plan saved = planRepository.save(plan);

        // Creator is attending by default
        PlanRsvp rsvp = new PlanRsvp(saved, creator, PlanRsvp.RsvpStatus.ATTENDING);
        planRsvpRepository.save(rsvp);
        saved.getRsvps().add(rsvp);

        PlanDto dto = new PlanDto(saved);
        realtimePublisher.publishPlanEvent(roomId, "PLAN_CREATED", dto);

        return dto;
    }

    @Transactional
    public PlanDto updateRsvp(Long planId, User user, PlanRsvp.RsvpStatus status) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + planId));

        PlanRsvp rsvp = planRsvpRepository.findByPlanIdAndUserId(planId, user.getId())
                .orElse(new PlanRsvp(plan, user, status));

        rsvp.setRsvpStatus(status);
        planRsvpRepository.save(rsvp);

        PlanDto dto = new PlanDto(plan);
        realtimePublisher.publishPlanEvent(plan.getRoom().getId(), "RSVP_UPDATED", dto);
        return dto;
    }
}
