package org.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("admin")
public class AdminController {

  @Value("${api.admin-key}")
  private String adminKey;


/*  @GetMapping("users")
  List<UserDto> getUsers(@RequestHeader("x-admin-key") String adminKeyHeader) {
    if (!adminKey.equals(adminKeyHeader)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    List<UUID> userIds = mongoTemplate.query(PostEntity.class)
        .distinct("userId")
        .as(UUID.class)
        .all();

    var result = new ArrayList<UserDto>();
    for (var userId : userIds) {
      result.add(UserDto.builder().uuid(userId.toString()).build());
    }

    return result;
  }*/
}
