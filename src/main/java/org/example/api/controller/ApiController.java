package org.example.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.dto.PostCreationResponseDto;
import org.example.api.dto.PostDto;
import org.example.api.dto.PostUpsertDto;
import org.example.api.dto.UserDto;
import org.example.api.entity.PostEntity;
import org.example.api.entity.UserEntity;
import org.example.api.repository.PostRepository;
import org.example.api.repository.UserRepository;
import org.example.api.service.Verification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ApiController {

  public static final String ALLOW_OWNER = "hasRole('USER') and #userId.toString() == principal";
  public static final String ALLOW_ADMIN_AND_OWNER = "hasRole('ADMIN') or (" + ALLOW_OWNER +")";
  public static final String ALLOW_GUEST = "hasRole('GUEST')";

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @PostMapping("/users/{userId}/posts")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize(ApiController.ALLOW_OWNER)
  PostCreationResponseDto addPost(@PathVariable UUID userId, @RequestBody PostUpsertDto body) {

    UserEntity user = new UserEntity();
    user.setId(userId);

    PostEntity entity = new PostEntity();
    entity.setUser(user);
    entity.setText(body.getText());
    entity.setCreatedAt(LocalDateTime.now());
    entity.setUpdatedAt(LocalDateTime.now());
    postRepository.saveAndFlush(entity);

    return PostCreationResponseDto.builder().postId(entity.getId().toString()).build();
  }

  @DeleteMapping("/users/{userId}/posts/{postId}")
  @PreAuthorize(ApiController.ALLOW_ADMIN_AND_OWNER)
  void deletePosts(@PathVariable UUID userId, @PathVariable UUID postId) {
    Optional<PostEntity> postEntityOptional = postRepository.findById(postId);
    Verification.isPostPresent(postEntityOptional);
    postRepository.delete(postEntityOptional.get());
  }

  @PutMapping("/users/{userId}/posts/{postId}")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize(ApiController.ALLOW_OWNER)
  void updatePosts(@PathVariable UUID userId,
                   @PathVariable UUID postId,
                   @RequestBody PostUpsertDto body) {
    Optional<PostEntity> postEntityOptional = postRepository.findById(postId);
    Verification.isPostPresent(postEntityOptional);

    PostEntity postEntity = postEntityOptional.get();
    postEntity.setText(body.getText());
    postEntity.setUpdatedAt(LocalDateTime.now());
    postRepository.save(postEntity);
  }

  //Public
  @GetMapping("users")
  @PreAuthorize(ApiController.ALLOW_GUEST)
  Slice<UserDto> getUsers(Pageable pageable) {
    return userRepository.findBy(pageable).map(ApiController::userMapper);
  }

  @GetMapping("/users/{userId}/posts")
  @PreAuthorize(ApiController.ALLOW_GUEST)
  List<PostDto> getPosts(@PathVariable UUID userId, Pageable pageable) {
    List<PostEntity> entities = postRepository.findByUserId(userId);
    var result = new ArrayList<PostDto>();
    for (var entity : entities) {
      result.add(postMapper(entity));
    }

    return result;
  }

  @GetMapping("/users/{userId}/posts/{postId}")
  @PreAuthorize(ApiController.ALLOW_GUEST)
  PostDto getPosts(@PathVariable UUID userId, @PathVariable UUID postId) {
    Optional<PostEntity> postEntityOptional = postRepository.findById(postId);
    Verification.isPostPresent(postEntityOptional);

    return postMapper(postEntityOptional.get());
  }


  private static PostDto postMapper(PostEntity entity) {
    return PostDto.builder()
        .id(entity.getId().toString())
        .text(entity.getText())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  private static UserDto userMapper(UserEntity entity) {
    return UserDto.builder()
        .uuid(entity.getId().toString())
        .firstname(entity.getFirstname())
        .lastname(entity.getLastname())
        .createdAt(entity.getCreatedAt())
        .build();
  }
}
