package org.example.api.service;

import org.example.api.dto.PostDto;
import org.example.api.dto.UserDto;
import org.example.api.entity.PostEntity;
import org.example.api.entity.UserEntity;

public class Mappers {

  public static PostDto postMapper(PostEntity entity) {
    return PostDto.builder()
        .id(entity.getId().toString())
        .text(entity.getText())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }



  public static UserDto userMapper(UserEntity entity) {
    return UserDto.builder()
        .uuid(entity.getId().toString())
        .firstname(entity.getFirstname())
        .lastname(entity.getLastname())
        .createdAt(entity.getCreatedAt())
        .build();
  }

  private Mappers() {}
}
