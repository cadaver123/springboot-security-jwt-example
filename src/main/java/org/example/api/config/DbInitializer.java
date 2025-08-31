package org.example.api.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.entity.AuthorityEntity;
import org.example.api.entity.PostEntity;
import org.example.api.entity.UserEntity;
import org.example.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
@RequiredArgsConstructor
@Slf4j
public class DbInitializer {

  private final List<String> names = List.of("John", "Emily", "Michael", "Sarah", "David", "Jessica", "Robert", "Jennifer", "Daniel", "Lisa");

  private final UserRepository repository;
  private final JdbcTemplate jdbcTemplate;
  private final PasswordEncoder passwordEncoder;
  private final Random random = new Random();

  @Value("classpath:lorem_ipsum.txt")
  private Resource loremIpsumFile;

  @PostConstruct
  void initDb() throws IOException {
    String loremIpsum = loremIpsumFile.getContentAsString(StandardCharsets.UTF_8);

    String sql = "SELECT count(*) FROM users";
    if (jdbcTemplate.queryForObject(sql, Long.class) > 0) {
      log.info("Db already initialized");
      return;
    }

    var adminEntity = UserEntity.builder().username("admin").firstname("John").lastname("Admin").password(passwordEncoder.encode("Admin123")).authorities(List.of(AuthorityEntity.builder().authority("ROLE_ADMIN").build())).createdAt(LocalDateTime.now()).build();
    repository.save(adminEntity);

    for (var name : names) {
      List<PostEntity> postsList = new ArrayList<>();
      for (int i = random.nextInt(11); i > 0; i--) {
        int randomLength = (int) (loremIpsum.length() * (1.0*(1 + random.nextInt(99)))/loremIpsum.length());
        postsList.add(PostEntity.builder().text(loremIpsum.substring(0, randomLength)).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build());
      }

      var userEntity = UserEntity.builder().username(name.toLowerCase()).firstname(name).lastname("User").password(passwordEncoder.encode(name+"123")).authorities(List.of(AuthorityEntity.builder().authority("ROLE_ADMIN").build())).createdAt(LocalDateTime.now()).posts(postsList).build();
      repository.save(userEntity);
    }
  }
}
