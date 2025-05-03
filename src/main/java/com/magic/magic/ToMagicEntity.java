package com.magic.magic;

import com.magic.hero.ToHeroEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToMagicEntity implements Converter<MagicDto, Magic> {

  private final ToHeroEntity toHeroEntity;

  public ToMagicEntity(ToHeroEntity toHeroEntity) {
    this.toHeroEntity = toHeroEntity;
  }

  @Override
  public Magic convert(MagicDto source) {
    Magic magic = new Magic();
    magic.setId(source.id());
    magic.setName(source.name());
    magic.setDescription(source.description());
//    magic.setOwner(toHeroEntity.convert(source.owner()));
    return magic;
  }
}
