package com.magic.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

  @Value("${api.endpoint.baseUrl}")
  private String url;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(request -> request
            .requestMatchers(HttpMethod.GET, url + "/magics/**").permitAll()
            .requestMatchers(HttpMethod.GET, url + "/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.POST, url + "/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.PUT, url + "/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.DELETE, url + "/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
            .anyRequest().authenticated()
    )
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())
            .headers(header -> header
                    .frameOptions(Customizer.withDefaults()).disable())
    ;
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
