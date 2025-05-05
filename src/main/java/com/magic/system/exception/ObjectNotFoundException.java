package com.magic.system.exception;

public class ObjectNotFoundException extends Exception {

  public ObjectNotFoundException(String name, String id) {
    super("Could not find "+ name + " with id " + id);
  }

  public ObjectNotFoundException(String name, Integer id) {
    super("Could not find "+ name + " with id " + id);
  }

  public ObjectNotFoundException(String name, Long id) {
    super("Could not find "+ name + " with id " + id);
  }
}
