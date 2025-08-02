package org.blog.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostDto {
  private String id;
  private String text;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
