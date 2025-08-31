package org.example.api.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostUpsertDto {
  @NotBlank(message = "Text is mandatory")
  @Size(min = 1, max = 50, message = "Text must be less than 50 characters")
  private String text;
}
