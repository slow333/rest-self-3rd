package com.magic.security;

import com.magic.client.redis.RedisCacheClient;
import com.magic.user.MyUserPrincipal;
import com.magic.user.SiteUser;
import com.magic.user.dto.UserDto;
import com.magic.user.converter.ToSiteUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

  private final JwtProvider jwtProvider;
  private final ToSiteUserDto toSiteUserDto;
  private final RedisCacheClient redisCacheClient;

  public Map<String, Object> createLoginInfo(Authentication authentication) {
    // create user
    MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
    SiteUser siteUser = principal.getSiteUser();
    UserDto userDto = toSiteUserDto.convert(siteUser);
    // create token
    String token = jwtProvider.generateToken(authentication);
    redisCacheClient.set("whiteList:" + siteUser.getId(), token, 2, TimeUnit.HOURS);

    Map<String, Object> loginResultMap = new HashMap<>();
    loginResultMap.put("userInfo", userDto);
    loginResultMap.put("token", token);

    return loginResultMap;
  }
}
