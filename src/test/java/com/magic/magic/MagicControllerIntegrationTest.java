package com.magic.magic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class MagicControllerIntegrationTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;

  @Value("${api.endpoint.baseUrl}/magics")
  String url;

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void findMagicByIdSuccess() throws Exception {
    mockMvc.perform(get(url + "/002").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.data.id").value("002"));
  }
  @Test
  void findMagicByIdNotFound() throws Exception {
    mockMvc.perform(get(url + "/009").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find magic with id 009"))
            .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void findAllSuccess() throws Exception {
    mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Find All Success."))
            .andExpect(jsonPath("$.data.content", Matchers.hasSize(6)));
  }
  @Test
  void addSuccess() throws Exception {
    MagicDto dto = new MagicDto(null, "add name", "add desc", null);
    String json = objectMapper.writeValueAsString(dto);
    mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
                    .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Add Magic Success."))
            .andExpect(jsonPath("$.data.id").isNotEmpty())
            .andExpect(jsonPath("$.data.name").value("add name"));
  }
  @Test
  void deleteSuccess() throws Exception {
    mockMvc.perform(delete(url+"/002").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Delete Magic Success."))
            .andExpect(jsonPath("$.data").isEmpty());
  }
  @Test
  void deleteNotFound() throws Exception {
    mockMvc.perform(delete(url+"/009").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find magic with id 009"))
            .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void updateSuccess() throws Exception {
    MagicDto dto = new MagicDto("002", "update name", "update desc", null);
    String json = objectMapper.writeValueAsString(dto);
    mockMvc.perform(put(url+"/002").accept(MediaType.APPLICATION_JSON)
                    .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Update Magic Success."))
            .andExpect(jsonPath("$.data.name").value("update name"));
  }
  @Test
  void updateNotFound() throws Exception {
    MagicDto dto = new MagicDto("009", "update name", "update desc", null);
    String json = objectMapper.writeValueAsString(dto);
    mockMvc.perform(put(url+"/009").accept(MediaType.APPLICATION_JSON)
                    .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find magic with id 009"));
  }

  @Test
  void findByDescription() throws Exception {
    Map<String, String> searchCriteria = new HashMap<>();
    searchCriteria.put("description", "거미줄");
    String json = objectMapper.writeValueAsString(searchCriteria);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("page", "0");
    params.add("size", "2");
    params.add("sort", "name,desc");

    mockMvc.perform(post(url + "/search").params(params).accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Find Magic By Query Success."))
            .andExpect(jsonPath("$.data.content", Matchers.hasSize(1)));
  }
}
