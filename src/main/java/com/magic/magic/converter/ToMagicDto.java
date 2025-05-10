package com.magic.magic.converter;

import com.magic.hero.converter.ToHeroDto;
import com.magic.magic.Magic;
import com.magic.magic.dto.MagicDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToMagicDto implements Converter<Magic, MagicDto> {

  private final ToHeroDto toHeroDto;

  public ToMagicDto(ToHeroDto toHeroDto) {
    this.toHeroDto = toHeroDto;
  }

  @Override
  public MagicDto convert(Magic source) {
    MagicDto dto = new MagicDto(
            source.getId(),
            source.getName(),
            source.getDescription(),
            source.getOwner() != null
                    ? toHeroDto.convert(source.getOwner()) : null);
    return dto;
  }
}
