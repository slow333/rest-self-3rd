package com.magic.hero;

import com.magic.system.Result;
import com.magic.system.StatusCode;
import com.magic.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/heroes")
@RequiredArgsConstructor
public class HeroController {

  private final HeroService heroService;
  private final ToHeroDto toHeroDto;
  private final ToHeroEntity toHeroEntity;

  @GetMapping("/{heroId}")
  public Result findHeroById(@PathVariable Integer heroId) throws ObjectNotFoundException {
    Hero hero = heroService.findById(heroId);
    HeroDto dto = toHeroDto.convert(hero);

    return new Result(true, StatusCode.SUCCESS, "Find Hero Success.", dto);
  }
  @GetMapping
  public Result findHeroAll(){
    List<Hero> heroes = heroService.findAll();
    List<HeroDto> heroDtos = heroes.stream().map(toHeroDto::convert).toList();
    return new Result(true, StatusCode.SUCCESS, "Find All Success.", heroDtos);
  }

  @DeleteMapping("/{heroId}")
  public Result deleteHero(@PathVariable Integer heroId) throws ObjectNotFoundException {
    heroService.delete(heroId);
    return new Result(true, StatusCode.SUCCESS, "Delete Success.");
  }

  @PostMapping
  public Result addHero(@RequestBody HeroDto heroDto) {
    Hero hero = toHeroEntity.convert(heroDto);
    Hero newHero = heroService.add(hero);
    HeroDto dto = toHeroDto.convert(newHero);
    return new Result(true, StatusCode.SUCCESS, "Add Hero Success.", dto);
  }

  @PutMapping("/{heroId}")
  public Result updateHero(
          @PathVariable Integer heroId,
          @RequestBody HeroDto heroDto) throws ObjectNotFoundException {
    Hero converted = toHeroEntity.convert(heroDto);
    Hero update = heroService.update(heroId, converted);
    HeroDto dto = toHeroDto.convert(update);
    return new Result(true, StatusCode.SUCCESS, "Update Hero Success.", dto);
  }
  @PatchMapping("/{heroId}/magics/{magicId}")
  public Result assignMagic(@PathVariable Integer heroId,
                            @PathVariable String magicId) throws ObjectNotFoundException {
    heroService.assignMagic(heroId, magicId);
    return new Result(true, StatusCode.SUCCESS, "Magic Assignment Success.");
  }
}
