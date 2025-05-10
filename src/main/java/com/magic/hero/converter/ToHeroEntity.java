package com.magic.hero.converter;

import com.magic.hero.Hero;
import com.magic.hero.dto.HeroDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToHeroEntity implements Converter<HeroDto, Hero> {
  @Override
  public Hero convert(HeroDto source) {
    Hero h = new Hero();
    h.setId(source.id());
    h.setName(source.name());
    return h;
  }
}
