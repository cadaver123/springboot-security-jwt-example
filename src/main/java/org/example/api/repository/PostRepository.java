package org.example.api.repository;

import org.example.api.entity.PostEntity;
import org.example.api.entity.projections.PostsCountProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {

   Slice<PostEntity> findByUserId(UUID userId, Pageable pageable);


   @Query("SELECT p.userId as userId, COUNT(p) as count FROM PostEntity p GROUP BY p.userId")
   List<PostsCountProjection> getPostsCountPerUser();
}
