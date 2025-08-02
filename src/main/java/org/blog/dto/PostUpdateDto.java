package org.blog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostUpdateDto {
  private String text;
}
