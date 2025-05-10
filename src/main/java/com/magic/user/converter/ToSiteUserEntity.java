package com.magic.user.converter;

import com.magic.user.SiteUser;
import com.magic.user.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToSiteUserEntity implements Converter<UserDto, SiteUser> {
  @Override
  public SiteUser convert(UserDto source) {
    SiteUser su = new SiteUser();
    su.setId(source.id());
    su.setUsername(source.username());
    su.setRoles(source.roles());
    su.setEnabled(source.enabled());
    return su;
  }
}
