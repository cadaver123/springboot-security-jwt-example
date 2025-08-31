package org.example.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
  private String uuid;
  private String firstname;
  private String lastname;
  private String username;
  private List<String> roles;
  private LocalDateTime createdAt;
}
