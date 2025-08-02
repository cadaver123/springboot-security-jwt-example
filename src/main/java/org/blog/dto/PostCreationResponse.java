package org.blog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreationResponse {
  private String postId;
}
