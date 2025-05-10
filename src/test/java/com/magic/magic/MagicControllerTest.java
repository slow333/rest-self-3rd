package com.magic.magic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magic.magic.dto.MagicDto;
import com.magic.system.IdWorker;
import com.magic.system.StatusCode;

import com.magic.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class MagicControllerTest {

  @MockitoBean
  private MagicService magicService;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Mock
  IdWorker idWorker;

  @Value("${api.endpoint.baseUrl}/magics")
  String url;

  List<Magic> magics;

  @BeforeEach
  void setUp() {
    magics = new ArrayList<>();
    Magic m1 = new Magic();
    m1.setId("001");
    m1.setName("fly");
    m1.setDescription("날라다니는 능력");

    Magic m2 = new Magic();
    m2.setId("002");
    m2.setName("hide");
    m2.setDescription("숨는 능력");

    Magic m3 = new Magic();
    m3.setId("003");
    m3.setName("power");
    m3.setDescription("힘이 쌤");

//    Hero h1 = new Hero();
//    h1.setId(1);
//    h1.setName("Super man");
//    m1.setOwner(h1);

    magics.add(m1);
    magics.add(m2);
    magics.add(m3);
  }

  @Test
  void findMagicByIdSuccess() throws Exception {
    // Given.
    given(magicService.findById("001")).willReturn(magics.get(0));
    // When and Then.
    mockMvc.perform(get( url + "/001").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find One Success."))
            .andExpect(jsonPath("$.data.id").value("001"))
            .andExpect(jsonPath("$.data.name").value("fly"));
  }

  @Test
  void findMagicByIdNotFound() throws Exception {
    // Given.
    given(magicService.findById(Mockito.anyString())).willThrow(new ObjectNotFoundException("magic", "009"));
    // When and Then.
    mockMvc.perform(get(url + "/009").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find magic with id 009"))
            .andExpect(jsonPath("$.data").isEmpty());
  }

  /*@Test
  void findAllSuccess() throws Exception {
    // Given.
    given(magicService.findAll()).willReturn(magics);
    // When and Then.
    mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find All Success."))
            .andExpect(jsonPath("$.data.content", Matchers.hasSize(3)));
  }*/

  @Test
  void findAllPageSuccess() throws Exception {
    // Given.
    Pageable pageable = PageRequest.of(0, 2);
    PageImpl<Magic> magicPage = new PageImpl<>(magics, pageable, magics.size());

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("page", "0");
    params.add("size", "2");
    params.add("sort", "name,desc");

    given(magicService.findAll(Mockito.any(Pageable.class))).willReturn(magicPage);
    // When and Then.
    mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON).params(params))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find All Success."));
  }

  @Test
  void addMagicSuccess() throws Exception {
    // Given
    MagicDto magicDto = new MagicDto("009", "new Magic", "신규 능력", null);
    Magic magic = new Magic();
    magic.setId("999");
    magic.setName("new Magic");
    magic.setDescription("신규 능력");
    magic.setOwner(null);

    String json = objectMapper.writeValueAsString(magicDto);
    given(idWorker.nextId()).willReturn(999L);
    given(magicService.add(Mockito.any(Magic.class))).willReturn(magic);
    // When and Then
    mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Add Magic Success."))
            .andExpect(jsonPath("$.data.id").isNotEmpty())
            .andExpect(jsonPath("$.data.description").value("신규 능력"));
  }
  @Test
  void updateMagicSuccess() throws Exception {
    // Given
    MagicDto update = new MagicDto("009",
            "update Magic", "update 능력", null);
    Magic magic = new Magic();
    magic.setId("009");
    magic.setName("update Magic");
    magic.setDescription("update 능력");
    magic.setOwner(null);

    String json = objectMapper.writeValueAsString(update);
    given(magicService.update(eq("009"), Mockito.any(Magic.class))).willReturn(magic);
    // When and Then
    mockMvc.perform(put(url+"/009").accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Update Magic Success."))
            .andExpect(jsonPath("$.data.id").value("009"))
            .andExpect(jsonPath("$.data.description").value("update 능력"));
  }
  @Test
  void updateMagicNotFound() throws Exception {
    // Given
    MagicDto update = new MagicDto("009",
            "update Magic", "update 능력", null);

    String json = objectMapper.writeValueAsString(update);
    given(magicService.update(eq("009"), Mockito.any(Magic.class)))
            .willThrow(new ObjectNotFoundException("magic", "009"));
    // When and Then
    mockMvc.perform(put(url+"/009").accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message")
                    .value("Could not find magic with id 009"));
  }
  @Test
  void deleteMagicSuccess() throws Exception {
    // Given
    doNothing().when(magicService).delete("002");
    // When and Then
    mockMvc.perform(delete(url+"/002").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Delete Magic Success."));
  }
  @Test
  void deleteMagicNotFound() throws Exception {
    // Given
    doThrow(new ObjectNotFoundException("magic","002")).when(magicService).delete("002");
    // When and Then
    mockMvc.perform(delete(url+"/002").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find magic with id 002"));
  }
}