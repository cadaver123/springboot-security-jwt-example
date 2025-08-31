package org.example.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateDto {

  @NotBlank(message = "Username is mandatory")
  @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
  @Pattern(
      regexp = "^[a-zA-Z0-9_]+$",
      message = "Username can only contain letters, numbers, and underscores"
  )
  private String username;

  @NotBlank(message = "Firstname is mandatory")
  @Size(max = 50, message = "Firstname must be less than 50 characters")
  private String firstname;

  @NotBlank(message = "Lastname is mandatory")
  @Size(max = 50, message = "Lastname must be less than 50 characters")
  private String lastname;

  @NotBlank(message = "Password is mandatory")
  @Size(min = 6, max = 50, message = "Password must be at least 6 and at most 50 characters")
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
      message = "Password must contain at least one digit, one lowercase, and one uppercase letter"
  )
  private String password;
}
