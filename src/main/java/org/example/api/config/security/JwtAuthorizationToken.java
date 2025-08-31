package org.example.api.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class JwtAuthorizationToken extends AbstractAuthenticationToken {
  public static final JwtAuthorizationToken GUEST_TOKEN = new JwtAuthorizationToken(null, List.of(new SimpleGrantedAuthority("ROLE_GUEST")));

  private final String token;

  @Setter
  private String principal;

  public JwtAuthorizationToken(String token) {
    super(Collections.emptyList());
    this.token = token;
  }

  public JwtAuthorizationToken(String token, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.token = token;
  }


  @Override
  public Object getCredentials() {
    return null;
  }

}
