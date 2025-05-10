package com.magic.user.converter;

import com.magic.user.SiteUser;
import com.magic.user.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToSiteUserDto implements Converter<SiteUser, UserDto> {
  @Override
  public UserDto convert(SiteUser source) {
    UserDto dto = new UserDto(
            source.getId(),
            source.getUsername(),
            source.getRoles(),
            source.isEnabled()
    );
    return dto;
  }
}
