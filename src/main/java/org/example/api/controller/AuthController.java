package org.example.api.controller;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.example.api.config.security.UserDetailsModel;
import org.example.api.dto.TokenDto;
import org.example.api.dto.UserCreateDto;
import org.example.api.entity.AuthorityEntity;
import org.example.api.entity.UserEntity;
import org.example.api.repository.UserRepository;
import org.example.api.service.PemKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserRepository repository;
  private final PemKeyService pemKeyService;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("token")
  TokenDto getToken() {
    var userDetails = (UserDetailsModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    String token = Jwts.builder()
        .issuer("org.example.api")
        .audience().add("example-api").and()
        .subject(userDetails.getGuid().toString())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1000 * 3600))
        .signWith(pemKeyService.getPrivateKey())
        .compact();


    return new TokenDto(token, "Bearer", 3599);
  }

  @PostMapping("signup")
  @ResponseStatus(HttpStatus.CREATED)
  void addUser(@RequestBody @Validated UserCreateDto dto) {
    repository.saveAndFlush(UserEntity.builder()
        .username(dto.getUsername())
        .password(passwordEncoder.encode(dto.getPassword()))
        .firstname(dto.getFirstname())
        .lastname(dto.getLastname())
        .authorities(List.of(AuthorityEntity.builder().authority("ROLE_USER").build()))
        .createdAt(LocalDateTime.now())
        .build());
  }
}
