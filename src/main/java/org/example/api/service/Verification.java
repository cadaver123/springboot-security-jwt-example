package org.example.api.service;

import org.example.api.entity.PostEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class Verification {

  public static void isPostPresent(Optional<PostEntity> postEntityOptional) {
    if (postEntityOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  private Verification() { }
}
