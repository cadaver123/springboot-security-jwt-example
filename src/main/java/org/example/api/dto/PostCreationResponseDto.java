package org.example.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreationResponseDto {
  private String postId;
}
