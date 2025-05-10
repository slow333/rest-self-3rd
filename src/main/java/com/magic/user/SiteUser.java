package com.magic.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SiteUser {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotEmpty(message = "username is required.")
  private String username;

  @NotEmpty(message = "password is required.")
  private String password;

  @NotEmpty(message = "roles are required.")
  private String roles;

  private boolean enabled;

}
