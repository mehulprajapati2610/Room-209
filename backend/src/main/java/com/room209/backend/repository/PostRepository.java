package com.room209.backend.repository;

import com.room209.backend.entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByRoomId(Long roomId, Sort sort);
    List<Post> findByRoomIdAndCategory(Long roomId, Post.Category category, Sort sort);
}
