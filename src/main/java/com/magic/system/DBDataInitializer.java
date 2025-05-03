package com.magic.system;

import com.magic.hero.Hero;
import com.magic.hero.HeroRepository;
import com.magic.magic.Magic;
import com.magic.magic.MagicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBDataInitializer implements CommandLineRunner {

  private final HeroRepository heroRepository;
  private final MagicRepository magicRepository;

  @Override
  public void run(String... args) throws Exception {
    Magic m1 = new Magic();
    m1.setId("001");
    m1.setName("fly");
    m1.setDescription("날라다니는 능력");

    Magic m2 = new Magic();
    m2.setId("002");
    m2.setName("hide");
    m2.setDescription("숨는 능력");

    Magic m3 = new Magic();
    m3.setId("003");
    m3.setName("power");
    m3.setDescription("힘이 쌤");

    Magic m4 = new Magic();
    m4.setId("004");
    m4.setName("spider");
    m4.setDescription("거미줄 쏨");

    Magic m5 = new Magic();
    m5.setId("005");
    m5.setName("laser");
    m5.setDescription("레이져를 쏨");

    Magic m6 = new Magic();
    m6.setId("006");
    m6.setName("insight");
    m6.setDescription("상대방을 알아봄");

    Hero h1 = new Hero();
//    h1.setId(1);
    h1.setName("Super man");
    h1.addMagic(m1);
    h1.addMagic(m2);

    Hero h2 = new Hero();
//    h2.setId(2);
    h2.setName("X man");
    h2.addMagic(m3);
    h2.addMagic(m5);


    Hero h3 = new Hero();
//    h3.setId(3);
    h3.setName("Spider man");
    h3.addMagic(m4);

    heroRepository.save(h1);
    heroRepository.save(h2);
    heroRepository.save(h3);

    magicRepository.save(m6);
  }
}
