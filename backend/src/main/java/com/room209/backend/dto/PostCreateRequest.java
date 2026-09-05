package com.room209.backend.dto;

import com.room209.backend.entity.Post;

public class PostCreateRequest {
    private Post.Category category = Post.Category.CHIT_CHAT;
    private String content;
    private String mediaUrl;

    public PostCreateRequest() {}

    public Post.Category getCategory() { return category; }
    public void setCategory(Post.Category category) { this.category = category; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
}
