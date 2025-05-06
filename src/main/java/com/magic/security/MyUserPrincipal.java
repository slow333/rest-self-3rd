package com.magic.security;

import com.magic.user.SiteUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class MyUserPrincipal implements UserDetails {

  private SiteUser siteUser;
  public MyUserPrincipal(SiteUser siteUser) {
    this.siteUser = siteUser;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(StringUtils.tokenizeToStringArray(siteUser.getRoles(), " "))
            .map(role -> new SimpleGrantedAuthority("ROLE_"+role)).toList();
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
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
  public SiteUser getUser() {
    return siteUser;
  }
}
