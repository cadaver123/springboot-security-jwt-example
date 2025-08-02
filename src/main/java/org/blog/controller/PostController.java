package org.blog.controller;

import lombok.RequiredArgsConstructor;
import org.blog.dto.PostCreateDto;
import org.blog.dto.PostCreationResponse;
import org.blog.dto.PostDto;
import org.blog.entity.PostEntity;
import org.blog.entity.UserEntity;
import org.blog.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("blog")
public class PostController {

  private final PostRepository repository;

  @GetMapping("/users/{userId}/posts")
  List<PostDto> getPosts(@PathVariable UUID userId) {
    List<PostEntity> entities = repository.findByUserId(userId);
    var result = new ArrayList<PostDto>();
    for (var entity : entities) {
      result.add(postMapper(entity));
    }

    return result;
  }

  @PostMapping("/users/{userId}/posts")
  PostCreationResponse addPost(@PathVariable UUID userId, @RequestBody PostCreateDto body) {

    UserEntity user = new UserEntity();
    user.setId(userId);

    PostEntity entity = new PostEntity();
    entity.setUser(user);
    entity.setText(body.getText());
    entity.setCreatedAt(LocalDateTime.now());
    entity.setUpdatedAt(LocalDateTime.now());
    repository.saveAndFlush(entity);

    return PostCreationResponse.builder().postId(entity.getId().toString()).build();
  }

  @GetMapping("/users/{userId}/posts/{postId}")
  PostDto getPosts(@PathVariable UUID userId, @PathVariable UUID postId) {
    Optional<PostEntity> postEntityOptional = repository.findById(postId);
    postCheck(userId, postEntityOptional);

    return postMapper(postEntityOptional.get());
  }

  @DeleteMapping("/users/{userId}/posts/{postId}")
  void deletePosts(@PathVariable UUID userId, @PathVariable UUID postId) {
    Optional<PostEntity> postEntityOptional = repository.findById(postId);
    postCheck(userId, postEntityOptional);
    repository.delete(postEntityOptional.get());
  }

  @PutMapping("/users/{userId}/posts/{postId}")
  void updatePosts(@PathVariable UUID userId,
                   @PathVariable UUID postId,
                   @RequestBody PostCreateDto body) {
    Optional<PostEntity> postEntityOptional = repository.findById(postId);
    postCheck(userId, postEntityOptional);

    PostEntity postEntity = postEntityOptional.get();
    postEntity.setText(body.getText());
    postEntity.setUpdatedAt(LocalDateTime.now());
    repository.save(postEntity);
  }

  private static void postCheck(UUID userId, Optional<PostEntity> postEntityOptional) {
    if (postEntityOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    if (!userId.equals(postEntityOptional.get().getUserId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
  }

  private static PostDto postMapper(PostEntity entity) {
    return PostDto.builder()
        .id(entity.getId().toString())
        .text(entity.getText())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}
