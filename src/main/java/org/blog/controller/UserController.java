package org.blog.controller;

import lombok.RequiredArgsConstructor;
import org.blog.dto.UserCreateDto;
import org.blog.dto.UserCreationResponse;
import org.blog.entity.UserEntity;
import org.blog.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("blog/users")
@RequiredArgsConstructor
public class UserController {
  private final UserRepository repository;

  @PostMapping
  UserCreationResponse addUser(@RequestBody UserCreateDto body) {

    UserEntity entity = new UserEntity();
    entity.setFirstname(body.getFirstname());
    entity.setLastname(body.getLastname());
    repository.saveAndFlush(entity);

    return UserCreationResponse.builder().userId(entity.getId().toString()).build();
  }
}
