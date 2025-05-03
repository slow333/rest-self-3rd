package com.magic.utils;

import com.magic.hero.Hero;

public class HeroGenerator {

  static public Hero getH1() {
    Hero h = new Hero();
    h.setId(1);
    h.setName("hero 1");
    return h;
  }

  static public Hero getH2() {
    Hero h = new Hero();
    h.setId(2);
    h.setName("hero 2");
    return h;
  }
  static public Hero getH3() {
    Hero h = new Hero();
    h.setId(3);
    h.setName("hero 3");
    return h;
  }
}
