package com.room209.backend.dto;

import com.room209.backend.entity.Post;
import java.time.LocalDateTime;

public class PostDto {
    private Long id;
    private UserDto author;
    private Post.Category category;
    private String content;
    private String mediaUrl;
    private int likesCount;
    private int commentsCount;
    private LocalDateTime createdAt;

    public PostDto() {}

    public PostDto(Post post) {
        this.id = post.getId();
        this.author = new UserDto(post.getAuthor());
        this.category = post.getCategory();
        this.content = post.getContent();
        this.mediaUrl = post.getMediaUrl();
        this.likesCount = post.getLikesCount();
        this.commentsCount = post.getComments() != null ? post.getComments().size() : 0;
        this.createdAt = post.getCreatedAt();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDto getAuthor() { return author; }
    public void setAuthor(UserDto author) { this.author = author; }

    public Post.Category getCategory() { return category; }
    public void setCategory(Post.Category category) { this.category = category; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public int getCommentsCount() { return commentsCount; }
    public void setCommentsCount(int commentsCount) { this.commentsCount = commentsCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
