package com.magic.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToSiteUserDto implements Converter<SiteUser, SiteUserDto> {
  @Override
  public SiteUserDto convert(SiteUser source) {
    SiteUserDto dto = new SiteUserDto(
            source.getId(), source.getUsername(), source.getRoles(), source.isEnabled()
    );
    return dto;
  }
}
