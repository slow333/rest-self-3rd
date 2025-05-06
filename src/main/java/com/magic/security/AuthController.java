package com.magic.security;

import com.magic.system.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
@RequestMapping("${api.endpoint.baseUrl}/users")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public Result getLoginInfo(Authentication authentication) {
    log.info("authentication: " + authentication.getName());
    return new Result(true, 200, "Login Success.",
            authService.createLoginInfo(authentication));
  }
}
