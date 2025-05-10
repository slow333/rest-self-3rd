package com.magic.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class CustomBasicAuthEntryPoint implements AuthenticationEntryPoint {

  private final HandlerExceptionResolver resolver;

  public CustomBasicAuthEntryPoint(@Qualifier("handlerExceptionResolver")
                                   HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    response.addHeader("WWW-Authenticate", "Basic realm=\"Realm\""); // httpBasic에서 사용하기 위해 필요.
    this.resolver.resolveException(request, response, null, authException);
  }
}
