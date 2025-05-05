package com.magic.system.exception;

public class UsernameNotFoundException extends Exception {
  public UsernameNotFoundException(String username) {
    super("Could not find user with username " + username);
  }
}
