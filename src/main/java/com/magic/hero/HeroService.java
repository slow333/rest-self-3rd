package com.magic.hero;

import com.magic.magic.Magic;
import com.magic.magic.MagicRepository;
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
  private final MagicRepository magicRepository;

  public Hero findById(Integer heroId) throws ObjectNotFoundException {
    return heroRepository.findById(heroId)
            .orElseThrow(() -> new ObjectNotFoundException("hero", heroId));
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
    return heroRepository.save(hero);
  }

  public Hero update(Integer heroId, Hero update) throws ObjectNotFoundException {
    Hero oldHero = heroRepository.findById(heroId)
            .orElseThrow(() -> new ObjectNotFoundException("hero", heroId));
    oldHero.setName(update.getName());
    return heroRepository.save(oldHero);
  }

  public void assignMagic(Integer heroId, String magicId) throws ObjectNotFoundException {
    Hero hero = heroRepository.findById(heroId)
            .orElseThrow(() -> new ObjectNotFoundException("hero", heroId));
    Magic magicToBeAssigned = magicRepository.findById(magicId)
            .orElseThrow(() -> new ObjectNotFoundException("magic", magicId));
    if (magicToBeAssigned.getOwner() != null) {
      magicToBeAssigned.getOwner().removeMagic(magicToBeAssigned);
    }
    hero.addMagic(magicToBeAssigned);
  }
}
