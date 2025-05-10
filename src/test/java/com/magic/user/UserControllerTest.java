package com.magic.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magic.system.StatusCode;
import com.magic.system.exception.ObjectNotFoundException;
import com.magic.user.dto.UserDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

  @MockitoBean
  private UserService userService;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Value("${api.endpoint.baseUrl}/users")
  String url;

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
    users.add(su1);
    users.add(su2);
  }

  @Test
  void findAll() throws Exception {
    given(userService.findAll()).willReturn(users);

    mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Find All Success."))
            .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].username").value("admin"));
  }
  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void findByIdSuccess() throws Exception {
    given(userService.findById(1L)).willReturn(users.get(0));

    mockMvc.perform(get(url + "/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find User Success."))
            .andExpect(jsonPath("$.data.id").value(1L))
            .andExpect(jsonPath("$.data.username").value("admin"));
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void findByIdNotFound() throws Exception {
    given(userService.findById(5L))
            .willThrow(new ObjectNotFoundException("user", 5L));

    mockMvc.perform(get(url + "/5").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find user with id 5"));
  }

  @Test
  void createUserSuccess() throws Exception {
    SiteUser su = new SiteUser();
    su.setId(5L);
    su.setUsername("test");
    su.setPassword("<PASSWORD>");
    su.setRoles("user");
    su.setEnabled(true);

    String json = objectMapper.writeValueAsString(su);

    given(userService.createUser(Mockito.any(SiteUser.class))).willReturn(su);

    mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Create User Success."))
            .andExpect(jsonPath("$.data.id").isNotEmpty())
            .andExpect(jsonPath("$.data.enabled").value(true))
            .andExpect(jsonPath("$.data.username").value("test"));
  }

  @Test
  void updateUserSuccess() throws Exception {
    UserDto update = new UserDto(1L, "admin", "admin", false);

    SiteUser old = new SiteUser();
    old.setId(1L);
    old.setUsername("admin-update");
    old.setRoles("admin user");
    old.setEnabled(true);


    String json = objectMapper.writeValueAsString(update);
    given(userService.updateUser(eq(1L), Mockito.any(SiteUser.class))).willReturn(old);

    mockMvc.perform(put(url+"/1").accept(MediaType.APPLICATION_JSON)
            .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Update User Success."))
            .andExpect(jsonPath("$.data.id").value(1L))
            .andExpect(jsonPath("$.data.username").value("admin-update"))
            .andExpect(jsonPath("$.data.roles").value("admin user"))
            .andExpect(jsonPath("$.data.enabled").value(true));
  }

  @Test
  void updateUserNotFound() throws Exception {
    UserDto update = new UserDto(6L, "admin", "admin", false);

    String json = objectMapper.writeValueAsString(update);
    given(userService.updateUser(eq(6L), Mockito.any(SiteUser.class)))
            .willThrow(new ObjectNotFoundException("user", 6L));

    mockMvc.perform(put(url+"/6").accept(MediaType.APPLICATION_JSON)
                    .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find user with id 6"))
            .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void deleteUserSuccess() throws Exception {
    doNothing().when(userService).deleteUser(1L);

    mockMvc.perform(delete(url+"/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Delete User Success."));
  }

  @Test
  void deleteUserNotFound() throws Exception {
    doThrow(new ObjectNotFoundException("user", 9L))
            .when(userService).deleteUser(Mockito.anyLong());

    mockMvc.perform(delete(url+"/9").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find user with id 9"));
  }
}