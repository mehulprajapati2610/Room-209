package com.room209.backend.dto;

import com.room209.backend.entity.Comment;
import java.time.LocalDateTime;

public class CommentDto {
    private Long id;
    private UserDto author;
    private String content;
    private LocalDateTime createdAt;

    public CommentDto() {}

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.author = new UserDto(comment.getAuthor());
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDto getAuthor() { return author; }
    public void setAuthor(UserDto author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
