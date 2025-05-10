package com.magic.security;

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

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

  private final JwtProvider jwtProvider;
  private final ToSiteUserDto toSiteUserDto;

  public Map<String, Object> createLoginInfo(Authentication authentication) {
    // create user
    MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
    SiteUser siteUser = principal.getSiteUser();
    UserDto userDto = toSiteUserDto.convert(siteUser);
    // create token
    String token = jwtProvider.generateToken(authentication);

    Map<String, Object> loginResultMap = new HashMap<>();
    loginResultMap.put("userInfo", userDto);
    loginResultMap.put("token", token);

    return loginResultMap;
  }
}
