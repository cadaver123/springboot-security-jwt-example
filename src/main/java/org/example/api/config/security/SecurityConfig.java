package org.example.api.config.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.api.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  //
  // Configuration for JWT authentication
  //

  private static final String AUTH_PATH = "/auth/**";
  private static final String SWAGGER_PATH = "/swagger-ui/**";
  private static final String API_DOCS = "/v3/api-docs/**";
  private static final String AUTH_PATH_TOKEN = "/auth/token";

  private final JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;
  private final UserDetailsServiceImpl userDetailsService;

  @Bean
  SecurityFilterChain appSecurityFilterChain(HttpSecurity http, AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
    http.securityMatcher(new NegatedRequestMatcher(
        new OrRequestMatcher(
            PathPatternRequestMatcher.withDefaults().matcher(AUTH_PATH),
            PathPatternRequestMatcher.withDefaults().matcher(SWAGGER_PATH),
            PathPatternRequestMatcher.withDefaults().matcher(API_DOCS)
        )))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .addFilterBefore(new JwtTokenAuthenticationFilter(jwtAuthenticationManager()), UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

    return http.build();
  }

  @Bean
  static RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role("ADMIN").implies("USER")
        .role("USER").implies("GUEST")
        .build();
  }


  @Bean
  public AuthenticationManager jwtAuthenticationManager() {
    return new ProviderManager(List.of(jwtTokenAuthenticationProvider));
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain signingFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher(PathPatternRequestMatcher.withDefaults().matcher(AUTH_PATH_TOKEN))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .authenticationProvider(daoAuthenticationProvider())
        .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint()));


    return http.build();
  }

  @Bean
  AuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;

  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
  }
}
