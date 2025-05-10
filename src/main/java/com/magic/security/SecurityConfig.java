package com.magic.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class SecurityConfig {

  private final RSAPublicKey publicKey;
  private final RSAPrivateKey privateKey;
  private final CustomBasicAuthEntryPoint basicAuthEntryPoint;
  private final CustomBearerTokenAuthEntryPoint bearerTokenAuthEntryPoint;
  private final CustomBearerTokenAccessDeniedHandler bearerTokenAccessDeniedHandler;

  public SecurityConfig(CustomBasicAuthEntryPoint basicAuthEntryPoint, CustomBearerTokenAuthEntryPoint bearerTokenAuthEntryPoint, CustomBearerTokenAccessDeniedHandler bearerTokenAccessDeniedHandler)
          throws NoSuchAlgorithmException {
    this.basicAuthEntryPoint = basicAuthEntryPoint;
    this.bearerTokenAuthEntryPoint = bearerTokenAuthEntryPoint;
    this.bearerTokenAccessDeniedHandler = bearerTokenAccessDeniedHandler;

    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();

    this.publicKey = (RSAPublicKey) keyPair.getPublic();
    this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
  }

  @Value("${api.endpoint.baseUrl}")
  private String url;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(request -> request
            .requestMatchers(HttpMethod.GET, url + "/magics/**").permitAll()
            .requestMatchers(HttpMethod.GET, url + "/magics/search").permitAll()
            .requestMatchers(HttpMethod.GET, url + "/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.POST, url + "/users").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.PUT, url + "/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.DELETE, url + "/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
            .anyRequest().authenticated()
    )
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .httpBasic(httpBasic -> httpBasic
                .authenticationEntryPoint(this.basicAuthEntryPoint))
        .headers(header -> header
                .frameOptions(Customizer.withDefaults()).disable())
        .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
                .authenticationEntryPoint(this.bearerTokenAuthEntryPoint)
                .accessDeniedHandler(this.bearerTokenAccessDeniedHandler))
        .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    ;
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSource);
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }
}
