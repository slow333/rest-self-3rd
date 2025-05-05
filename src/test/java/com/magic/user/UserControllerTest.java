package com.magic.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magic.system.StatusCode;
import com.magic.system.exception.ObjectNotFoundException;
import com.magic.system.exception.UsernameNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  @MockitoBean
  private UserService userService;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Value("${api.endpoint.baseUrl}/users")
  String url;

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

  @Test
  void findByUsernameSuccess() throws Exception {
    // Given.
    SiteUser su = new SiteUser();
    su.setId(1L);
    su.setUsername("user");
    su.setPassword("123");
    su.setRoles("admin");
    su.setEnabled(true);
    given(userService.findByUsername("user")).willReturn(su);

    mockMvc.perform(get(url + "/user").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find User Success."))
            .andExpect(jsonPath("$.data.id").value(1L))
            .andExpect(jsonPath("$.data.username").value("user"));
  }
  @Test
  void findByUsernameNotFound() throws Exception {
    // Given.
    given(userService.findByUsername(Mockito.anyString()))
            .willThrow(new UsernameNotFoundException("test"));

    mockMvc.perform(get(url + "/test").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find user with username test"));
  }

  @Test
  void findAll() throws Exception {
    given(userService.findAll()).willReturn(users);

    mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Find All Success."))
            .andExpect(jsonPath("$.data", Matchers.hasSize(2)));
  }

  @Test
  void createUserSuccess() throws Exception {
    SiteUser su = new SiteUser();
    su.setId(3L);
    su.setUsername("test");
    su.setPassword("<PASSWORD>");
    su.setRoles("user");
    su.setEnabled(true);

    String json = objectMapper.writeValueAsString(su);

    given(userService.createUser(Mockito.any(SiteUser.class))).willReturn(su);

    mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Create User Success."))
            .andExpect(jsonPath("$.data.id").value(3L))
            .andExpect(jsonPath("$.data.username").value("test"));
  }

  @Test
  void updateUserSuccess() throws Exception {
    SiteUser old = new SiteUser();
    old.setId(1L);
    old.setUsername("admin");
    old.setPassword("<PASSWORD>");
    old.setRoles("admin");
    old.setEnabled(true);

    SiteUser update = new SiteUser();
    update.setId(1L);
    update.setUsername("newUser");
    update.setPassword("<PASSWORD>");
    update.setRoles("admin user");
    update.setEnabled(false);

    String json = objectMapper.writeValueAsString(update);
    given(userService.updateUser(eq(1L), Mockito.any(SiteUser.class))).willReturn(update);

    mockMvc.perform(put(url+"/1").accept(MediaType.APPLICATION_JSON)
            .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Update User Success."))
            .andExpect(jsonPath("$.data.id").value(1L))
            .andExpect(jsonPath("$.data.username").value("newUser"));
  }
  @Test
  void updateUserNotFound() throws Exception {
    SiteUser old = new SiteUser();
    old.setId(1L);
    old.setUsername("admin");
    old.setPassword("<PASSWORD>");
    old.setRoles("admin");
    old.setEnabled(true);

    SiteUser update = new SiteUser();
    update.setId(1L);
    update.setUsername("newUser");
    update.setPassword("<PASSWORD>");
    update.setRoles("admin user");
    update.setEnabled(false);

    String json = objectMapper.writeValueAsString(update);
    given(userService.updateUser(eq(1L), Mockito.any(SiteUser.class))).willThrow(new ObjectNotFoundException("user", 1L));

    mockMvc.perform(put(url+"/1").accept(MediaType.APPLICATION_JSON)
                    .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find user with id 1"))
            .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void deleteUserSuccess() throws Exception {
    SiteUser old = new SiteUser();
    old.setId(1L);
    old.setUsername("admin");
    old.setPassword("<PASSWORD>");
    old.setRoles("admin");
    old.setEnabled(true);

    doNothing().when(userService).deleteUser(1L);

    mockMvc.perform(delete(url+"/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Delete User Success."));
  }
  @Test
  void deleteUserNotFound() throws Exception {
    doThrow(new ObjectNotFoundException("user", 9L)).when(userService).deleteUser(Mockito.anyLong());

    mockMvc.perform(delete(url+"/9").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find user with id 9"));
  }
}