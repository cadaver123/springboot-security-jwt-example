package org.example.api.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authorizationHeader = request.getHeader("Authorization");
    Authentication authenticate;
    if (authorizationHeader == null || !authorizationHeader.contains("Bearer ")) {
      log.trace("No bearer token.");
      authenticate = JwtAuthorizationToken.GUEST_TOKEN;
    } else {
      JwtAuthorizationToken authorizationToken = new JwtAuthorizationToken(authorizationHeader.replace("Bearer ", ""));
      authenticate = authenticationManager.authenticate(authorizationToken);
    }

    SecurityContextHolder.getContext().setAuthentication(authenticate);
    filterChain.doFilter(request, response);
  }
}
