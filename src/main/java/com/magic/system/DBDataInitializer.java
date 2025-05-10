package com.magic.system;

import com.magic.hero.Hero;
import com.magic.hero.HeroRepository;
import com.magic.magic.Magic;
import com.magic.magic.MagicRepository;
import com.magic.user.SiteUser;
import com.magic.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBDataInitializer implements CommandLineRunner {

  private final HeroRepository heroRepository;
  private final MagicRepository magicRepository;
  private final UserService userService;

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

    Hero h3 = new Hero();
//    h3.setId(3);
    h3.setName("Spider man");
    h3.addMagic(m4);

    Hero h4 = new Hero();
//    h3.setId(4);
    h3.setName("Wonder woman");
    h3.addMagic(m5);

    heroRepository.save(h1);
    heroRepository.save(h2);
    heroRepository.save(h3);
    heroRepository.save(h4);

    magicRepository.save(m6);

    SiteUser su1 = new SiteUser();
//    su1.setId(1L);
    su1.setUsername("admin");
    su1.setPassword("321");
    su1.setRoles("admin");
    su1.setEnabled(true);

    SiteUser su2 = new SiteUser();
//    su2.setId(2L);
    su2.setUsername("user");
    su2.setPassword("123");
    su2.setRoles("user");
    su2.setEnabled(true);

    SiteUser su3 = new SiteUser();
//    su3.setId(3L);
    su3.setUsername("guest");
    su3.setPassword("123");
    su3.setRoles("user");
    su3.setEnabled(true);

    SiteUser su4 = new SiteUser();
//    su4.setId(4L);
    su4.setUsername("anonymous");
    su4.setPassword("123");
    su4.setRoles("user");
    su4.setEnabled(false);

    userService.createUser(su1);
    userService.createUser(su2);
    userService.createUser(su3);
    userService.createUser(su4);

    System.out.println("DB Data Initialized");
    System.out.println("--------------------------------------");
    System.out.println("id: " + su1.getId() + ", username: " + su1.getUsername());
    System.out.println("id: " + su2.getId() + ", username: " + su2.getUsername());
    System.out.println("id: " + su3.getId() + ", username: " + su3.getUsername());
    System.out.println("id: " + su4.getId() + ", username: " + su4.getUsername());
    System.out.println("--------------------------------------");
  }
}
