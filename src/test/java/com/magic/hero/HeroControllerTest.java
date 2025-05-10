package com.magic.hero;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magic.hero.dto.HeroDto;
import com.magic.system.exception.ObjectNotFoundException;
import com.magic.utils.HeroGenerator;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class HeroControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  HeroService heroService;

  @Value("${api.endpoint.baseUrl}/heroes")
  String url;

  List<Hero> heroList;
  @BeforeEach
  void setUp() {
    heroList = new ArrayList<>();
    Hero h1 = HeroGenerator.getH1();
    Hero h2 = HeroGenerator.getH2();
    heroList.add(h1);
    heroList.add(h2);
  }

  @Test
  void findAllHeroSuccess() throws Exception {
    // Given.
    given(heroService.findAll()).willReturn(heroList);
    // When
    mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Find All Success."))
            .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].name").value("hero 1"));
  }

  @Test
  void findHeroByIdSuccess() throws Exception {
    // Given.
    Hero h1 = HeroGenerator.getH1();
    given(heroService.findById(1)).willReturn(h1);
    // When
    mockMvc.perform(get(url + "/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Find Hero Success."))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.name").value("hero 1"));
  }
  @Test
  void findHeroByIdNotFound() throws Exception {
    // Given.
    given(heroService.findById(Mockito.anyInt())).willThrow(new ObjectNotFoundException("hero", 9));
    // When
    mockMvc.perform(get(url + "/9").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find hero with id 9"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
  @Test
  void deleteHeroByIdSuccess() throws Exception {
    // Given.
    doNothing().when(heroService).delete(1);
    // When
    mockMvc.perform(delete(url + "/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Delete Success."));
  }
  @Test
  void addHeroSuccess() throws Exception {
    // Given.
    Hero h1 = new Hero();
    h1.setId(5);
    h1.setName("new Hero");
    HeroDto dto = new HeroDto(null, "new Hero", 0);

    String json = objectMapper.writeValueAsString(dto);
    given(heroService.add(Mockito.any(Hero.class))).willReturn(h1);

    // When
    mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
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

    HeroDto dto = new HeroDto(2, "update Hero", 0);

    String json = objectMapper.writeValueAsString(dto);
    given(heroService.update(eq(2), Mockito.any(Hero.class))).willReturn(update);

    // When
    mockMvc.perform(put(url + "/2").accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.message").value("Update Hero Success."))
            .andExpect(jsonPath("$.data.id").value(2))
            .andExpect(jsonPath("$.data.name").value("update Hero"));
  }
  @Test
  void updateHeroNotFound() throws Exception {
    // Given.
    HeroDto dto = new HeroDto(9, "update Hero", 0);

    String json = objectMapper.writeValueAsString(dto);
    given(heroService.update(eq(9), Mockito.any(Hero.class)))
            .willThrow(new ObjectNotFoundException("hero", 9));

    // When
    mockMvc.perform(put(url + "/9").accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.message").value("Could not find hero with id 9"));
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void magicAssignSuccess() throws Exception {
    doNothing().when(heroService).assignMagic(3, "001");

    mockMvc.perform(patch(url + "/3/magics/004").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true));
  }
}