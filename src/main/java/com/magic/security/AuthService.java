package com.magic.security;

import com.magic.user.SiteUser;
import com.magic.user.SiteUserDto;
import com.magic.user.ToSiteUserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

  private final JwtProvider jwtProvider;
  private final ToSiteUserDto toSiteUserDto;

  public AuthService(JwtProvider jwtProvider, ToSiteUserDto toSiteUserDto) {
    this.jwtProvider = jwtProvider;
    this.toSiteUserDto = toSiteUserDto;
  }

  public Map<String, Object> createLoginInfo(Authentication authentication) {
    MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
    SiteUser siteUser = principal.getUser();
    SiteUserDto siteUserDto = toSiteUserDto.convert(siteUser);
    String token = jwtProvider.generateToken(authentication);
//    String token = "token is null";
    Map<String, Object> tokenMap = Map.of("userInfo", siteUserDto, "token", token);
    return tokenMap;
  }
}
