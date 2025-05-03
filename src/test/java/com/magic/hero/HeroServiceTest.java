package com.magic.hero;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magic.system.exception.ObjectNotFoundException;
import com.magic.utils.HeroGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HeroServiceTest {

  @Mock
  HeroRepository heroRepository;

  @Autowired
  ObjectMapper objectMapper;

  @InjectMocks
  HeroService heroService;

//  @Autowired
//  HeroGenerator heroGenerator;

  List<Hero> heroes = new ArrayList<>();
  @BeforeEach
  void setUp() {
    Hero h1 = HeroGenerator.getH1();
    Hero h2 = HeroGenerator.getH2();
    Hero h3 = HeroGenerator.getH3();
    heroes.add(h1);
    heroes.add(h2);
    heroes.add(h3);
  }

  @Test
  void findAllSuccess() throws ObjectNotFoundException {
    given(heroRepository.findAll()).willReturn(heroes);

    List<Hero> heroList = heroRepository.findAll();

    assertThat(heroList.size()).isEqualTo(3);
    verify(heroRepository, times(1)).findAll();
  }

  @Test
  void findByIdSuccess() throws ObjectNotFoundException {
    Hero h1 = HeroGenerator.getH1();
    given(heroRepository.findById(h1.getId())).willReturn(Optional.of(h1));

    Hero hero = heroService.findById(1);

    assertThat(hero.getId()).isEqualTo(1);
    assertThat(hero.getName()).isEqualTo("hero 1");
    verify(heroRepository, times(1)).findById(1);
  }

  @Test
  void findByIdNotFound() throws ObjectNotFoundException {
    given(heroRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

    Throwable thrown = catchThrowable(() -> {
      Hero hero = heroService.findById(1);
    });

    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find hero with id 1");
    verify(heroRepository, times(1)).findById(1);
  }
  @Test
  void deleteByIdSuccess() throws ObjectNotFoundException {
    Hero h1 = HeroGenerator.getH1();
    given(heroRepository.findById(h1.getId())).willReturn(Optional.of(h1));
    doNothing().when(heroRepository).deleteById(1);

    heroService.delete(1);

    verify(heroRepository, times(1)).deleteById(1);
  }
  @Test
  void deleteByIdNotFound() throws ObjectNotFoundException {
    given(heroRepository.findById(9)).willReturn(Optional.empty());

    Throwable thrown = catchThrowable(() -> {
      heroService.findById(9);
    });
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                    .hasMessage("Could not find hero with id 9");
    verify(heroRepository, times(1)).findById(9);
  }

  @Test
  void addHeroSuccess() throws ObjectNotFoundException, JsonProcessingException {
    Hero newHero = new Hero();
    newHero.setName("new Hero");

    given(heroRepository.save(newHero)).willReturn(newHero);

    Hero hero = heroService.add(newHero);

    assertThat(hero.getId()).isEqualTo(hero.getId());
    assertThat(hero.getName()).isEqualTo("new Hero");
    verify(heroRepository, times(1)).save(newHero);
  }
  @Test
  void updateHeroSuccess() throws ObjectNotFoundException, JsonProcessingException {
    // given
    Hero oldHero = new Hero();
    oldHero.setId(2);
    oldHero.setName("old Hero");

    Hero updateHero = new Hero();
    updateHero.setName("update Hero");

    given(heroRepository.findById(2)).willReturn(Optional.of(oldHero));
    given(heroRepository.save(oldHero)).willReturn(oldHero);
    // when
    Hero hero = heroService.update(2, updateHero);

    assertThat(hero.getId()).isEqualTo(oldHero.getId());
    assertThat(hero.getName()).isEqualTo("update Hero");
    verify(heroRepository, times(1)).save(oldHero);
  }
  @Test
  void updateHeroNotFound() throws ObjectNotFoundException, JsonProcessingException {
    // given
    Hero oldHero = new Hero();
    oldHero.setId(9);
    oldHero.setName("old Hero");

    given(heroRepository.findById(9)).willReturn(Optional.empty());
    // when
    Throwable thrown = catchThrowable(() -> {
      Hero hero = heroService.findById(9);
    });

    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find hero with id 9");
    verify(heroRepository, times(1)).findById(9);
  }
}