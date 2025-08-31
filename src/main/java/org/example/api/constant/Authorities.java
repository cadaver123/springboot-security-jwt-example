package org.example.api.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Authorities {
  ADMIN("ADMIN"),
  AUTHOR("AUTHOR");

  private String name;
}
