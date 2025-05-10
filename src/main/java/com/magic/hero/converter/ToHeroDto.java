package com.magic.hero.converter;

import com.magic.hero.Hero;
import com.magic.hero.dto.HeroDto;
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
