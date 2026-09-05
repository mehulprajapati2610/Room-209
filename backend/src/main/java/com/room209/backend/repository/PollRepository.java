package com.room209.backend.repository;

import com.room209.backend.entity.Poll;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findByRoomId(Long roomId, Sort sort);
    Optional<Poll> findFirstByRoomIdAndActiveTrueOrderByCreatedAtDesc(Long roomId);
}
