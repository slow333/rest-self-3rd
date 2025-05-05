package com.magic.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToSiteUserEntity implements Converter<SiteUserDto, SiteUser> {
  @Override
  public SiteUser convert(SiteUserDto source) {
    SiteUser su = new SiteUser();
    su.setId(source.id());
    su.setUsername(source.username());
    su.setRoles(source.roles());
    su.setEnabled(source.enabled());
    return su;
  }
}
