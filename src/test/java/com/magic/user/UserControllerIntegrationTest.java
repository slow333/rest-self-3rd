package com.magic.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magic.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class UserControllerIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Value("${api.endpoint.baseUrl}/users")
  String url;

  String token;

  @BeforeEach
  void setUp() throws Exception {
    ResultActions resultActions = mockMvc.perform(post(url + "/login")
            .with(httpBasic("admin","321")));
    MvcResult mvcResult = resultActions.andDo(print()).andReturn();
    String contentAsString = mvcResult.getResponse().getContentAsString();
    JSONObject jsonObject = new JSONObject(contentAsString);
    this.token = "Bearer " + jsonObject.getJSONObject("data").getString("token");
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void findAll() throws Exception {
    mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Find All Success."))
            .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void createUserSuccess() throws Exception {
    SiteUser su = new SiteUser();
    su.setUsername("test");
    su.setPassword("<PASSWORD>");
    su.setRoles("user");
    su.setEnabled(true);

    String json = objectMapper.writeValueAsString(su);

    mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Create User Success."))
            .andExpect(jsonPath("$.data.id").value(5L))
            .andExpect(jsonPath("$.data.username").value("test"));
  }

  @Test
  void updateUserSuccess() throws Exception {
    SiteUser update = new SiteUser();
    update.setId(1L);
    update.setUsername("admin-update");
    update.setRoles("admin");
    update.setEnabled(true);

    String json = objectMapper.writeValueAsString(update);

    mockMvc.perform(put(url+"/1").accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Update User Success."))
            .andExpect(jsonPath("$.data.id").value(1L))
            .andExpect(jsonPath("$.data.username").value("admin-update"));
  }
  @Test
  void updateUserNotFound() throws Exception {
    SiteUser update = new SiteUser();
    update.setUsername("newUser");
    update.setPassword("<PASSWORD>");
    update.setRoles("admin user");
    update.setEnabled(false);

    String json = objectMapper.writeValueAsString(update);

    mockMvc.perform(put(url+"/9").accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find user with id 9"))
            .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void deleteUserSuccess() throws Exception {
    mockMvc.perform(delete(url+"/4").accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Delete User Success."));
  }
  @Test
  void deleteUserNotFound() throws Exception {
    mockMvc.perform(delete(url+"/9").accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find user with id 9"));
  }
}