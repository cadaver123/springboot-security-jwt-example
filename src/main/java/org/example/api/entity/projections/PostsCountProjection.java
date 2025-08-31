package org.example.api.entity.projections;

import java.util.UUID;

public interface PostsCountProjection {
  UUID getUserId();
  Integer getCount();
}
