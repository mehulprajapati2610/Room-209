package com.room209.backend.service;

import com.room209.backend.dto.PollCreateRequest;
import com.room209.backend.dto.PollDto;
import com.room209.backend.entity.*;
import com.room209.backend.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;
    private final PollVoteRepository pollVoteRepository;
    private final RoomRepository roomRepository;
    private final RealtimePublisher realtimePublisher;

    public PollService(PollRepository pollRepository, PollOptionRepository pollOptionRepository,
                       PollVoteRepository pollVoteRepository, RoomRepository roomRepository,
                       RealtimePublisher realtimePublisher) {
        this.pollRepository = pollRepository;
        this.pollOptionRepository = pollOptionRepository;
        this.pollVoteRepository = pollVoteRepository;
        this.roomRepository = roomRepository;
        this.realtimePublisher = realtimePublisher;
    }

    @Transactional(readOnly = true)
    public PollDto getActivePoll(Long roomId, User currentUser) {
        Optional<Poll> pollOpt = pollRepository.findFirstByRoomIdAndActiveTrueOrderByCreatedAtDesc(roomId);
        if (pollOpt.isEmpty()) {
            return null;
        }

        Poll poll = pollOpt.get();
        Long votedOptionId = null;
        if (currentUser != null) {
            Optional<PollVote> vote = pollVoteRepository.findByPollIdAndUserId(poll.getId(), currentUser.getId());
            if (vote.isPresent()) {
                votedOptionId = vote.get().getOption().getId();
            }
        }

        return new PollDto(poll, currentUser != null ? currentUser.getId() : null, votedOptionId);
    }

    @Transactional
    public PollDto createPoll(Long roomId, User creator, PollCreateRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));

        // Mark previous polls inactive
        List<Poll> activePolls = pollRepository.findByRoomId(roomId, Sort.by(Sort.Direction.DESC, "createdAt"));
        for (Poll p : activePolls) {
            p.setActive(false);
            pollRepository.save(p);
        }

        Poll poll = new Poll(room, request.getQuestion(), creator);
        Poll saved = pollRepository.save(poll);

        List<PollOption> options = new ArrayList<>();
        if (request.getOptions() != null) {
            for (String optText : request.getOptions()) {
                if (optText != null && !optText.trim().isEmpty()) {
                    PollOption opt = new PollOption(saved, optText.trim());
                    options.add(pollOptionRepository.save(opt));
                }
            }
        }
        saved.setOptions(options);

        PollDto dto = new PollDto(saved, creator.getId(), null);
        realtimePublisher.publishPollEvent(roomId, "POLL_CREATED", dto);
        return dto;
    }

    @Transactional
    public PollDto vote(Long pollId, Long optionId, User voter) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found: " + pollId));

        if (!poll.isActive()) {
            throw new RuntimeException("This poll is closed.");
        }

        if (pollVoteRepository.existsByPollIdAndUserId(pollId, voter.getId())) {
            throw new RuntimeException("You have already voted on this poll.");
        }

        PollOption option = pollOptionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found: " + optionId));

        option.setVoteCount(option.getVoteCount() + 1);
        pollOptionRepository.save(option);

        PollVote vote = new PollVote(poll, option, voter);
        pollVoteRepository.save(vote);

        PollDto dto = new PollDto(poll, voter.getId(), optionId);
        realtimePublisher.publishPollEvent(poll.getRoom().getId(), "POLL_VOTED", dto);
        return dto;
    }
}
