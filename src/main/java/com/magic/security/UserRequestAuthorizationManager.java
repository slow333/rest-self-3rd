package com.magic.security;

import java.util.Map;
import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;
import org.springframework.security.authorization.AuthorizationDecision;

@Component
public class UserRequestAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final UriTemplate URI = new UriTemplate("/api/v1/users/{userId}");

    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier,
        RequestAuthorizationContext context){

      // URI에서 userId를 추출
      Map<String, String> uriVariables = URI.match(context.getRequest().getRequestURI());
      String userIdFromUri = uriVariables.get("userId");

      // jwt auth에서 userId를 추출
      Authentication authentication = authenticationSupplier.get();
      String userIdFromJwt = ((Jwt) authentication.getPrincipal()).getClaim("userId").toString();
      // role이 user 인지 확인
      boolean hasUserRole = authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_user"));

      // role이 admin 인지 확인
      boolean hasAdminRole = authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));

      // 2개의 아이디를 비교
      boolean userIdsMatch = userIdFromUri != null && userIdFromUri.equals(userIdFromJwt);
      return new AuthorizationDecision(hasAdminRole || (hasUserRole && userIdsMatch));
    }
}
