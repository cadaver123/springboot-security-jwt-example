package org.example.api.config.security;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.entity.UserEntity;
import org.example.api.repository.UserRepository;
import org.example.api.service.PemKeyService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {
  private final PemKeyService pemKeyService;
  private final UserRepository userRepository;

  @Override
  public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
    JwtAuthorizationToken authorizationToken = (JwtAuthorizationToken) authentication;
    String userId;
    try {
      userId = Jwts.parser()
          .verifyWith(pemKeyService.getPublicKey())
          .build()
          .parseSignedClaims(authorizationToken.getToken())
          .getPayload()
          .getSubject();
    } catch (Exception e) {
      log.debug("Problem with the token");
      return JwtAuthorizationToken.GUEST_TOKEN;
    }
    Optional<UserEntity> user = userRepository.findById(UUID.fromString(userId));

    if(user.isEmpty()) {
      log.debug("Couldn't find the user");
      return JwtAuthorizationToken.GUEST_TOKEN;
    }

    authorizationToken = new JwtAuthorizationToken(authorizationToken.getToken(), user.get().getGrantedAuthorities());
    authorizationToken.setPrincipal(userId);
    authorizationToken.setAuthenticated(true);

    return authorizationToken;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(JwtAuthorizationToken.class);
  }
}
