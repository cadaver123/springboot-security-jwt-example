package org.example.api.config.security;

import org.springframework.security.core.AuthenticationException;

public class JtwAuthenticationException extends AuthenticationException {
  public JtwAuthenticationException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public JtwAuthenticationException(String msg) {
    super(msg);
  }
}
