package org.example.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.AdminUpdateUserRolesDto;
import org.example.api.dto.UserDto;
import org.example.api.entity.AuthorityEntity;
import org.example.api.entity.UserEntity;
import org.example.api.entity.projections.PostsCountProjection;
import org.example.api.repository.PostRepository;
import org.example.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("admin")
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminController {

  public static final String WRONG_ADMIN_KEY_MESSAGE = "Wrong admin key";

  private final UserRepository userRepository;
  private final PostRepository postRepository;

  @Value("${api.admin-key}")
  private String adminKey;

  @GetMapping("users")
  List<UserDto> getUsers(@RequestHeader("x-admin-key") String adminKeyHeader, Pageable pageable) {
    if (!adminKey.equals(adminKeyHeader)) {
      log.warn(WRONG_ADMIN_KEY_MESSAGE);
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, WRONG_ADMIN_KEY_MESSAGE);
    }

    Slice<UserEntity> users = userRepository.findBy(pageable);

    return users.stream().map(e -> UserDto.builder().uuid(e.getId().toString()).firstname(e.getFirstname()).lastname(e.getLastname()).username(e.getUsername()).roles(e.getGrantedAuthorities().stream().map(SimpleGrantedAuthority::getAuthority).toList()).createdAt(e.getCreatedAt()).build()).toList();
  }

  @GetMapping("users/posts:count")
  Map<String, Integer> getUsersPostsCount(@RequestHeader("x-admin-key") String adminKeyHeader) {
    if (!adminKey.equals(adminKeyHeader)) {
      log.warn(WRONG_ADMIN_KEY_MESSAGE);
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, WRONG_ADMIN_KEY_MESSAGE);
    }

    List<PostsCountProjection> postsCountPerUser = postRepository.getPostsCountPerUser();

    return postsCountPerUser.stream().collect(Collectors.toMap(e -> e.getUserId().toString(), PostsCountProjection::getCount));
  }

  @PutMapping("users/{userId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  void addRole(@RequestHeader("x-admin-key") String adminKeyHeader, @PathVariable UUID userId, @RequestBody AdminUpdateUserRolesDto dto) {
    if (!adminKey.equals(adminKeyHeader)) {
      log.warn(WRONG_ADMIN_KEY_MESSAGE);
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, WRONG_ADMIN_KEY_MESSAGE);
    }

    Optional<UserEntity> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    UserEntity userEntity = user.get();
    userEntity.getAuthorities().clear();
    userEntity.getAuthorities().addAll(dto.getRoles().stream()
        .map(role -> new AuthorityEntity(null, userId, "ROLE_" + role)).toList());

    userRepository.saveAndFlush(userEntity);
  }
}
