package com.magic.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final JwtInterceptor jwtInterceptor;

  public WebConfig(JwtInterceptor jwtInterceptor) {
    this.jwtInterceptor = jwtInterceptor;
  }

  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtInterceptor).addPathPatterns("/**");
  }
}
