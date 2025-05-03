package com.magic.hero;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToHeroDto implements Converter<Hero, HeroDto> {

  @Override
  public HeroDto convert(Hero source) {
    HeroDto dto = new HeroDto(
            source.getId(), source.getName(), source.getNumberOfMagics());
    return dto;
  }
}
