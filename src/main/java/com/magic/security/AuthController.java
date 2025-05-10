package com.magic.security;

import com.magic.system.Result;
import com.magic.user.SiteUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.baseUrl}/users")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private static final Logger log = LoggerFactory.getLogger(AuthController.class);

  @PostMapping("/login")
  public Result getLoginInfo(Authentication authentication) {
    log.info("authentication: {}", authentication.getName());
    return new Result(true, 200, "Login Success.",
            authService.createLoginInfo(authentication));
  }
}
