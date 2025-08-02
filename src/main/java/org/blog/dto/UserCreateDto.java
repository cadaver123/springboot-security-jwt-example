package org.blog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateDto {
  private String firstname;
  private String lastname;
}
