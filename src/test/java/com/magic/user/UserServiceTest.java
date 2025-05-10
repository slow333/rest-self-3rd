package com.magic.user;

import com.magic.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

  List<SiteUser> users;
  @BeforeEach
  void setUp() {
    users = new ArrayList<>();
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

    SiteUser su3 = new SiteUser();
    su2.setId(3L);
    su2.setPassword("123");
    su2.setUsername("kim");
    su2.setRoles("user");
    su2.setEnabled(false);

    users.add(su1);
    users.add(su2);
    users.add(su3);
  }

  @Test
  void findAllSuccess() {
    given(userRepository.findAll()).willReturn(users);
    List<SiteUser> userList = userService.findAll();
    assertThat(userList.size()).isEqualTo(3);
    verify(userRepository, times(1)).findAll();
  }
  @Test
  void findByIdSuccess() throws ObjectNotFoundException {
    // Given
    SiteUser su = new SiteUser();
    su.setId(2L);
    su.setPassword("123");
    su.setUsername("user");
    su.setRoles("user");
    su.setEnabled(true);

    given(userRepository.findById(2L)).willReturn(Optional.of(su));
    // When
    SiteUser siteUser = userService.findById(2L);
    // Then
    assertThat(siteUser.getUsername()).isEqualTo(su.getUsername());
    assertThat(siteUser.getPassword()).isEqualTo(su.getPassword());
    assertThat(siteUser.getRoles()).isEqualTo(su.getRoles());
    assertThat(siteUser.isEnabled()).isEqualTo(su.isEnabled());
    verify(userRepository, times(1)).findById(2L);
  }

  @Test
  void findByIdNotFound() throws ObjectNotFoundException {
    // Given
    given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
    // When
    Throwable thrown = catchThrowable(() -> {
      SiteUser siteUser = userService.findById(8L);
    });
    // Then
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find user with id 8");
    verify(userRepository, times(1)).findById(Mockito.anyLong());
  }

  @Test
  void createUserSuccess() {
    SiteUser newUser = new SiteUser();
    newUser.setUsername("newUser");
    newUser.setPassword("password");
    newUser.setRoles("user");
    newUser.setEnabled(true);

    given(userRepository.save(newUser)).willReturn(newUser);
    given(passwordEncoder.encode(Mockito.anyString())).willReturn("password");

    SiteUser savedUser = userService.createUser(newUser);

    assertThat(savedUser.getUsername()).isEqualTo(newUser.getUsername());
    assertThat(savedUser.getPassword()).isEqualTo("password");
    assertThat(savedUser.getRoles()).isEqualTo(newUser.getRoles());
    assertThat(savedUser.isEnabled()).isEqualTo(newUser.isEnabled());
    verify(userRepository, times(1)).save(newUser);
  }

  @Test
  void updateUserSuccess() throws ObjectNotFoundException {
    SiteUser old = new SiteUser();
    old.setId(3L);
    old.setUsername("newUser");
    old.setPassword("password");
    old.setRoles("user");
    old.setEnabled(true);

    SiteUser update = new SiteUser();
    old.setUsername("newUser-update");
    old.setPassword("password");
    old.setRoles("user");
    old.setEnabled(true);

    given(userRepository.findById(3L)).willReturn(Optional.of(old));
    given(userRepository.save(old)).willReturn(old);

    SiteUser siteUser = new SiteUser();
    siteUser.setRoles("user");
    MyUserPrincipal myUserPrincipal = new MyUserPrincipal(siteUser);

    SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
    emptyContext.setAuthentication(new UsernamePasswordAuthenticationToken(myUserPrincipal, null, myUserPrincipal.getAuthorities()));
    SecurityContextHolder.setContext(emptyContext);

    SiteUser su = userService.updateUser(3L, update);

    assertThat(su.getId()).isEqualTo(3L);
    assertThat(su.getUsername()).isEqualTo(update.getUsername());

    verify(userRepository, times(1)).findById(3L);
    verify(userRepository, times(1)).save(old);
  }
  @Test
  void updateUserNotFound() throws ObjectNotFoundException {

    given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

    Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
              userService.updateUser(3L, Mockito.mock(SiteUser.class));
    });

    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find user with id 3");
    verify(userRepository, times(1)).findById(3L);
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

    given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

    Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
      userService.deleteUser(3L);
    });

    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find user with id 3");
  }
}