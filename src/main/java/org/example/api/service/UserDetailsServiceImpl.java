package org.example.api.service;

import lombok.RequiredArgsConstructor;
import org.example.api.config.security.UserDetailsModel;
import org.example.api.entity.UserEntity;
import org.example.api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = repository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("Username not found");
    }

    return new UserDetailsModel(user.getUsername(), user.getPassword(), user.getId(), user.getGrantedAuthorities());
  }
}
