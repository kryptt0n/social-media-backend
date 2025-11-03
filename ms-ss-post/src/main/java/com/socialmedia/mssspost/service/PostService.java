package com.socialmedia.mssspost.service;

import com.socialmedia.mssspost.dto.*;
import com.socialmedia.mssspost.entity.Post;
import com.socialmedia.mssspost.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final CursorService cursorService;


    public PostDto createPost(CreatePostRequestDto request) {
        Post post = Post.builder()
                .content(request.getContent())
                .username(request.getUsername())
                .createdAt(LocalDateTime.now())
                .build();
        return toResponse(postRepository.save(post));
    }


    public PostDto updatePost(Integer postId, UpdatePostRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        post.setContent(request.getContent());
        Post updated = postRepository.save(post);

        return toResponse(updated);
    }


    public void deletePost(Integer id) {
        if (!postRepository.existsById(id)) {
//            throw new RuntimeException("Post not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        postRepository.deleteById(id);
    }

    //TODO: Finish scroll response with hasMore and cursor for next post
    public PostResponseDto getAllPosts() {

        PostResponseDto response = PostResponseDto.builder()
                .posts(postRepository.findAll()
                        .stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList()))
                .cursor("")
                .hasMore(false)
                .build();

        return response;
    }

    public PostDto getPostById(Integer id) {
        return postRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    public PostResponseDto searchPosts(String keyword, String cursor, int pageSize) {
        LocalDateTime createdBefore = null;
        Integer idBefore = null;
        String nextCursor = null;
        log.warn("I got: keyword - " + keyword + " | cursor - " + cursor + " | PageSize : " + pageSize);

        if (cursor != null && !cursor.isBlank()) {
            var payload = cursorService.decode(cursor);
            createdBefore = payload.createdAt();
            idBefore = payload.id();
            log.warn("Cursor is not empty: " + createdBefore + " | " + idBefore);
        }

        int limitPlusOne = pageSize + 1;

        List<Post> fetchedPosts = (keyword == null || keyword.isBlank())
                ? postRepository.fetchFeed(createdBefore, idBefore, limitPlusOne)
                : postRepository.searchFeed(keyword, createdBefore, idBefore, limitPlusOne);

        boolean hasMore = fetchedPosts.size() > pageSize;
        if (hasMore) {
            log.warn("Has more posts! " + fetchedPosts.size() + " - size of fetched ");
            Post last = fetchedPosts.getLast();
            log.warn("Last post: " + last.getContent() + " - content and id:  " + last.getId());
            nextCursor = cursorService.encode(last.getCreatedAt(), last.getId());
            fetchedPosts = fetchedPosts.subList(0, pageSize);
        }

        List<PostDto> dtos = fetchedPosts.stream().map(this::toResponse).toList();

        PostResponseDto response = PostResponseDto.builder()
                .posts(dtos)
                .cursor(nextCursor)
                .hasMore(hasMore)
                .build();

        return response;
    }


    public PostResponseDto getPostsByUsername(String username, String cursor, int limit) {
        LocalDateTime createdBefore = null;
        String nextCursor = null;

        if (cursor != null && !cursor.isBlank()) {
            var payload = cursorService.decode(cursor);
            createdBefore = payload.createdAt();
        }

        int limitPlusOne = limit + 1;

        List<Post> fetchedPosts =  postRepository.fetchByUsername(username, createdBefore, limitPlusOne);

        boolean hasMore = fetchedPosts.size() > limit;
        if (hasMore) {
            Post last = fetchedPosts.getLast();
            nextCursor = cursorService.encode(last.getCreatedAt(), last.getId());
            fetchedPosts = fetchedPosts.subList(0, limit);
        }

        List<PostDto> dtos = fetchedPosts.stream().map(this::toResponse).toList();

        PostResponseDto response = PostResponseDto.builder()
                .posts(dtos)
                .cursor(nextCursor)
                .hasMore(hasMore)
                .build();

        return response;
    }


    public PostResponseDto getByFollowedUsernames(List<String> usernames, String cursor, int limit) {

        if (usernames == null || usernames.isEmpty()) {
            return PostResponseDto.builder()
                    .posts(List.of())
                    .cursor(null)
                    .hasMore(false)
                    .build();
        }

        LocalDateTime createdBefore = null;
        Integer idBefore = null;
        String nextCursor = null;

        if (cursor != null && !cursor.isBlank()) {
            var payload = cursorService.decode(cursor);
            createdBefore = payload.createdAt();
            idBefore = payload.id();
        }

        int limitPlusOne = limit + 1;

        List<Post> fetchedPosts = postRepository.fetchFollowed(usernames, createdBefore, idBefore, limitPlusOne);

        boolean hasMore = fetchedPosts.size() > limit;
        if (hasMore) {
            Post last = fetchedPosts.getLast();
            nextCursor = cursorService.encode(last.getCreatedAt(), last.getId());
            fetchedPosts = fetchedPosts.subList(0, limit);
        }

        List<PostDto> dtos = fetchedPosts.stream().map(this::toResponse).toList();

        PostResponseDto response = PostResponseDto.builder()
                .posts(dtos)
                .cursor(nextCursor)
                .hasMore(hasMore)
                .build();

        return response;
    }


    public PostResponseDto getReportedPosts() {

        PostResponseDto response = PostResponseDto.builder()
                .posts(postRepository.findByReported(true)
                        .stream()
                        .map(this::toResponse)
                        .toList())
                .cursor("LocalDateTime.now()")
                .hasMore(false)
                .build();

        return response;

    }


    public StatsResponseDto getStats() {
        StatsResponseDto statsDto = new StatsResponseDto();
        statsDto.setTotalPosts(postRepository.count());
        statsDto.setReportedPosts(postRepository.countByReportedTrue());

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        statsDto.setDailyPosts(postRepository.countByCreatedAtBetween(startOfDay, endOfDay));

        return statsDto;
    }

    private PostDto toResponse(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .username(post.getUsername())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public void reportPost(Integer postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        postRepository.reportPost(postId);
    }

    @Transactional
    public void deleteAllByUsername(String username) {
        postRepository.deleteAllByUsername(username);
    }
}
