package com.magic.user;

import com.magic.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  List<SiteUser> users = new ArrayList<>();
  @BeforeEach
  void setUp() {
    SiteUser su1 = new SiteUser();
    su1.setId(1L);
    su1.setPassword("321");
    su1.setUsername("admin");
    su1.setRoles("admin");
    su1.setEnabled(true);

    SiteUser su2 = new SiteUser();
    su2.setId(2L);
    su2.setPassword("123");
    su2.setUsername("user");
    su2.setRoles("user");
    su2.setEnabled(true);
    users.add(su1);
    users.add(su2);
  }

/*  @Test
  void findByUsernameSuccess() throws UsernameNotFoundException {
    // Given
    SiteUser su = new SiteUser();
    su.setId(2L);
    su.setPassword("123");
    su.setUsername("user");
    su.setRoles("user");
    su.setEnabled(true);

    given(userRepository.findByUsername(su.getUsername())).willReturn(Optional.of(su));
    // When
    SiteUser siteUser = userService.findByUsername("user");
    // Then
    assertThat(siteUser.getUsername()).isEqualTo(su.getUsername());
    assertThat(siteUser.getPassword()).isEqualTo(su.getPassword());
    assertThat(siteUser.getRoles()).isEqualTo(su.getRoles());
    assertThat(siteUser.isEnabled()).isEqualTo(su.isEnabled());
    verify(userRepository, times(1)).findByUsername(su.getUsername());
  }
  @Test
  void findByUsernameNotFound() throws ObjectNotFoundException {
    // Given
    given(userRepository.findByUsername(Mockito.anyString())).willReturn(Optional.empty());
    // When
    Throwable thrown = catchThrowable(() -> {
      SiteUser siteUser = userService.findByUsername("user");
    });
    // Then
    assertThat(thrown).isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("Could not find user with username user");
    verify(userRepository, times(1)).findByUsername(Mockito.anyString());
  }*/
  @Test
  void findAllSuccess() {
    given(userRepository.findAll()).willReturn(users);
    List<SiteUser> userList = userService.findAll();
    assertThat(userList.size()).isEqualTo(2);
    verify(userRepository, times(1)).findAll();
  }

  @Test
  void createUserSuccess() {
    SiteUser newUser = new SiteUser();
    newUser.setId(3L);
    newUser.setPassword("password");
    newUser.setUsername("newUser");
    newUser.setRoles("user");
    newUser.setEnabled(true);

    given(userRepository.save(newUser)).willReturn(newUser);
    given(passwordEncoder.encode(Mockito.anyString())).willReturn("password");

    SiteUser savedUser = userService.createUser(newUser);
    assertThat(savedUser.getId()).isEqualTo(newUser.getId());
    assertThat(savedUser.getUsername()).isEqualTo(newUser.getUsername());
    assertThat(savedUser.getRoles()).isEqualTo(newUser.getRoles());
    assertThat(savedUser.isEnabled()).isEqualTo(newUser.isEnabled());
    verify(userRepository, times(1)).save(newUser);
  }

  @Test
  void updateUserSuccess() throws ObjectNotFoundException {
    SiteUser old = new SiteUser();
    old.setId(3L);
    old.setPassword("password");
    old.setUsername("newUser");
    old.setRoles("user");
    old.setEnabled(true);

    SiteUserDto update = new SiteUserDto(3L, "newUser", "user admin", false);

    given(userRepository.findById(3L)).willReturn(Optional.of(old));
    given(userRepository.save(old)).willReturn(old);

    SiteUserDto siteUser = userService.updateUser(3L, update);

    assertThat(siteUser.id()).isEqualTo(update.id());
    assertThat(siteUser.username()).isEqualTo(update.username());
    assertThat(siteUser.roles()).isEqualTo(update.roles());
    assertThat(siteUser.enabled()).isEqualTo(update.enabled());
    verify(userRepository, times(1)).save(old);
  }
  @Test
  void updateUserNotFound() throws ObjectNotFoundException {
    SiteUser old = new SiteUser();
    old.setId(3L);
    old.setPassword("password");
    old.setUsername("newUser");
    old.setRoles("user");
    old.setEnabled(true);

    SiteUserDto update = new SiteUserDto(3L, "newUser", "user admin", false);

    given(userRepository.findById(3L)).willReturn(Optional.empty());
    Throwable thrown = catchThrowable(() -> {
      SiteUserDto siteUser = userService.updateUser(3L, update);
    });

    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find user with id 3");
  }

  @Test
  void deleteUserSuccess() throws ObjectNotFoundException {
    SiteUser su = new SiteUser();
    su.setId(3L);
    su.setPassword("password");
    su.setUsername("newUser");
    su.setRoles("user");
    su.setEnabled(true);
    given(userRepository.findById(3L)).willReturn(Optional.of(su));
    doNothing().when(userRepository).deleteById(3L);

    userService.deleteUser(3L);

    verify(userRepository, times(1)).deleteById(3L);
  }
  @Test
  void deleteUserNotFound() throws ObjectNotFoundException {
    SiteUser su = new SiteUser();
    su.setId(3L);
    su.setPassword("password");
    su.setUsername("newUser");
    su.setRoles("user");
    su.setEnabled(true);
    given(userRepository.findById(3L)).willReturn(Optional.empty());

    Throwable thrown = catchThrowable(() -> {
      userService.deleteUser(3L);
    });

    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find user with id 3");
  }
}