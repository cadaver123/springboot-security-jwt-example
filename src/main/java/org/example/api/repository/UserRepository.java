
package org.example.api.repository;

import org.example.api.entity.UserEntity;
import org.example.api.entity.projections.UserPostProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  UserEntity findByUsername(String username);

  @Query(value = "SELECT u.id as userId, p.id as postId  FROM UserEntity u JOIN u.posts p")
  Slice<UserPostProjection> getAllPostsIds(Pageable pageable);

  Slice<UserEntity> findByUsername(UUID userId, Pageable pageable);

  Slice<UserEntity> findBy(Pageable pageable);
}
