package com.magic.hero;

import com.magic.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HeroService {

  private final HeroRepository heroRepository;

  public Hero findById(Integer heroId) throws ObjectNotFoundException {
    Hero hero = heroRepository.findById(heroId)
            .orElseThrow(() -> new ObjectNotFoundException("hero", heroId));
    return hero;
  }

  public List<Hero> findAll() {
    return heroRepository.findAll();
  }

  public void delete(Integer heroId) throws ObjectNotFoundException {
    Hero hero = heroRepository.findById(heroId)
            .orElseThrow(() -> new ObjectNotFoundException("hero", heroId));
    if (hero.getMagics() != null) {
      hero.deleteAllMagic();
    }
    heroRepository.deleteById(heroId);
  }

  public Hero add(Hero hero) {
    Hero newHero = heroRepository.save(hero);
    return newHero;
  }

  public Hero update(Integer heroId, Hero update) throws ObjectNotFoundException {
    Hero oldHero = heroRepository.findById(heroId)
            .orElseThrow(() -> new ObjectNotFoundException("hero", heroId));
    oldHero.setName(update.getName());
    Hero updatedHero = heroRepository.save(oldHero);
    return updatedHero;
  }

}
