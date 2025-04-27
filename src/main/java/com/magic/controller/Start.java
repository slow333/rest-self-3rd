package com.magic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Start {
   @GetMapping("/start")
   public String start() {
      return "Magic application started!";
   }
}
