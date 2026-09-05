package com.room209.backend.repository;

import com.room209.backend.entity.Chore;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChoreRepository extends JpaRepository<Chore, Long> {
    List<Chore> findByRoomId(Long roomId, Sort sort);
    List<Chore> findByRoomIdAndCompleted(Long roomId, boolean completed, Sort sort);
}
