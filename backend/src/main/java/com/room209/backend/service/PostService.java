package com.room209.backend.service;

import com.room209.backend.dto.CommentDto;
import com.room209.backend.dto.CommentRequest;
import com.room209.backend.dto.PostCreateRequest;
import com.room209.backend.dto.PostDto;
import com.room209.backend.entity.Comment;
import com.room209.backend.entity.Post;
import com.room209.backend.entity.Room;
import com.room209.backend.entity.User;
import com.room209.backend.repository.CommentRepository;
import com.room209.backend.repository.PostRepository;
import com.room209.backend.repository.RoomRepository;
import com.room209.backend.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RealtimePublisher realtimePublisher;
    private final FCMService fcmService;

    public PostService(PostRepository postRepository, CommentRepository commentRepository,
                       RoomRepository roomRepository, UserRepository userRepository,
                       RealtimePublisher realtimePublisher, FCMService fcmService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.realtimePublisher = realtimePublisher;
        this.fcmService = fcmService;
    }

    @Transactional(readOnly = true)
    public List<PostDto> getFeed(Long roomId, Post.Category category) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<Post> posts;
        if (category == null || category == Post.Category.ALL) {
            posts = postRepository.findByRoomId(roomId, sort);
        } else {
            posts = postRepository.findByRoomIdAndCategory(roomId, category, sort);
        }
        return posts.stream().map(PostDto::new).collect(Collectors.toList());
    }

    @Transactional
    public PostDto createPost(Long roomId, User author, PostCreateRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));

        Post post = new Post(
                room,
                author,
                request.getCategory() != null ? request.getCategory() : Post.Category.CHIT_CHAT,
                request.getContent(),
                request.getMediaUrl()
        );

        Post saved = postRepository.save(post);
        PostDto postDto = new PostDto(saved);

        // Broadcast over STOMP
        realtimePublisher.publishFeedEvent(roomId, "POST_CREATED", postDto);

        // If announcement, trigger push notification to other roommates
        if (saved.getCategory() == Post.Category.ANNOUNCEMENT) {
            List<User> roommates = userRepository.findByRoomId(roomId);
            for (User member : roommates) {
                if (!member.getId().equals(author.getId()) && member.getFcmToken() != null) {
                    fcmService.sendPushNotification(
                            member.getFcmToken(),
                            "Room 209 Announcement",
                            author.getName() + ": " + saved.getContent()
                    );
                }
            }
        }

        return postDto;
    }

    @Transactional
    public CommentDto addComment(Long postId, User author, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));

        Comment comment = new Comment(post, author, request.getContent());
        Comment saved = commentRepository.save(comment);

        CommentDto dto = new CommentDto(saved);
        realtimePublisher.publishFeedEvent(post.getRoom().getId(), "COMMENT_ADDED", dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long postId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        return commentRepository.findByPostId(postId, sort).stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDto toggleLike(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));

        post.setLikesCount(post.getLikesCount() + 1);
        Post saved = postRepository.save(post);

        PostDto dto = new PostDto(saved);
        realtimePublisher.publishFeedEvent(post.getRoom().getId(), "POST_LIKED", dto);
        return dto;
    }
}
