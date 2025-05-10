package com.magic.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;


public class MyUserPrincipal implements UserDetails {

  private SiteUser siteUser;
  public MyUserPrincipal(SiteUser siteUser) {
    this.siteUser = siteUser;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(StringUtils.tokenizeToStringArray(siteUser.getRoles(), " "))
            .map(role -> new SimpleGrantedAuthority("ROLE_"+role))
            .toList();
  }

  @Override
  public String getPassword() {
    return siteUser.getPassword();
  }

  @Override
  public String getUsername() {
    return siteUser.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
  public SiteUser getSiteUser() {
    return siteUser;
  }
}
