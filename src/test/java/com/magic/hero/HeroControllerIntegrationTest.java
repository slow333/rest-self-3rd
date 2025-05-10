package com.magic.hero;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magic.hero.dto.HeroDto;
import com.magic.system.exception.ObjectNotFoundException;
import com.magic.utils.HeroGenerator;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class HeroControllerIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Value("${api.endpoint.baseUrl}/heroes")
  String url;

  String token;

  @BeforeEach
  void setUp() throws Exception {

    ResultActions resultActions = mockMvc.perform(post("/api/v1/users/login")
            .with(httpBasic("user","123")));
    MvcResult mvcResult = resultActions.andDo(print()).andReturn();
    String contentAsString = mvcResult.getResponse().getContentAsString();
    JSONObject jsonObject = new JSONObject(contentAsString);
    this.token = "Bearer " + jsonObject.getJSONObject("data").getString("token");
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void findAllHeroSuccess() throws Exception {
    mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON).header("Authorization", token))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Find All Success."))
            .andExpect(jsonPath("$.data", Matchers.hasSize(4)))
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].name").value("Super man"));
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void findHeroByIdSuccess() throws Exception {
    mockMvc.perform(get(url + "/1")
                    .accept(MediaType.APPLICATION_JSON).header("Authorization", token))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Find Hero Success."))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.name").value("Super man"));
  }
  @Test
  void findHeroByIdNotFound() throws Exception {
    mockMvc.perform(get(url + "/9")
                    .accept(MediaType.APPLICATION_JSON).header("Authorization", token))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find hero with id 9"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
  @Test
  void deleteHeroByIdSuccess() throws Exception {
    mockMvc.perform(delete(url + "/1").accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Delete Success."));
  }
  @Test
  void addHeroSuccess() throws Exception {
    // Given.
    Hero h1 = new Hero();
//    h1.setId(5);
    h1.setName("new Hero");
    HeroDto dto = new HeroDto(null, "new Hero", 0);

    String json = objectMapper.writeValueAsString(dto);

    // When
    mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Add Hero Success."))
            .andExpect(jsonPath("$.data.id").value(5))
            .andExpect(jsonPath("$.data.name").value("new Hero"));
  }
  @Test
  void updateHeroSuccess() throws Exception {
    // Given.
    Hero update = new Hero();
    update.setId(2);
    update.setName("update Hero");

    String json = objectMapper.writeValueAsString(update);

    // When
    mockMvc.perform(put(url + "/2").accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Update Hero Success."))
            .andExpect(jsonPath("$.data.id").value(2))
            .andExpect(jsonPath("$.data.name").value("update Hero"));
  }
  @Test
  void updateHeroNotFound() throws Exception {
    // Given.
    Hero update = new Hero();
    update.setName("update Hero");

    String json = objectMapper.writeValueAsString(update);

    // When
    mockMvc.perform(put(url + "/9").accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find hero with id 9"));
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void magicAssignSuccess() throws Exception {
    mockMvc.perform(patch(url + "/3/magics/004").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true));
  }
}