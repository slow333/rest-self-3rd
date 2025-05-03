package com.magic.magic;

import com.magic.hero.Hero;
import com.magic.system.IdWorker;
import com.magic.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MagicServiceTest {

  @Mock
  MagicRepository magicRepository;
  @Mock
  IdWorker idWorker;

  @InjectMocks
  MagicService magicService;

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

    Hero h1 = new Hero();
    h1.setId(1);
    h1.setName("Super man");
    m1.setOwner(h1);

    magics.add(m1);
    magics.add(m2);
    magics.add(m3);
  }

  @Test
  void findByIdSuccess() throws ObjectNotFoundException {
    // Given
    Magic m1 = new Magic();
    m1.setId("001");
    m1.setName("fly");
    m1.setDescription("날라다니는 능력");

    Hero h1 = new Hero();
    h1.setId(1);
    h1.setName("Super man");
    m1.setOwner(h1);

    given(magicRepository.findById(m1.getId())).willReturn(Optional.of(m1));
    // When
    Magic foundMagic = magicService.findById("001");
    // Then
    assertThat(foundMagic.getId()).isEqualTo(m1.getId());
    assertThat(foundMagic.getName()).isEqualTo(m1.getName());

    verify(magicRepository, times(1)).findById("001");
  }

  @Test
  void findByIdNotFound() {
    // Given
    given(magicRepository.findById(Mockito.anyString())).willReturn(Optional.empty());
    // When
    Throwable thrown = catchThrowable(() -> {
      Magic foundMagic = magicService.findById("001");
    });
    // Then
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find magic with id 001");
    verify(magicRepository, times(1)).findById("001");
  }
  @Test
  void findAllSuccess() {
    given(magicRepository.findAll()).willReturn(magics);
    List<Magic> magicsFound = magicService.findAll();
    assertThat(magicsFound.size()).isEqualTo(3);
    verify(magicRepository, times(1)).findAll();
  }
  @Test
  void addMagicSuccess() {
    // Given
    Magic magic = new Magic();
//    magic.setId(null);
    magic.setName("new ability");
    magic.setDescription("종합적인 능력");
    given(idWorker.nextId()).willReturn(9L);
    given(magicRepository.save(magic)).willReturn(magic);
    // When
    Magic addedMagic = magicService.add(magic);
    // Then
    assertThat(addedMagic.getId()).isEqualTo("9");
    assertThat(addedMagic.getDescription()).isEqualTo("종합적인 능력");
    verify(magicRepository, times(1)).save(magic);
  }
  @Test
  void updateMagicSuccess() throws ObjectNotFoundException {
    // Given
    Magic oldMagic = new Magic();
    oldMagic.setId("009");
    oldMagic.setName("new ability");
    oldMagic.setDescription("종합적인 능력");

    Magic update = new Magic();
    update.setId("009");
    update.setName("update ability");
    update.setDescription("종합적인 능력");

    given(magicRepository.findById(Mockito.anyString())).willReturn(Optional.of(oldMagic));
    given(magicRepository.save(oldMagic)).willReturn(update);

    // When
    Magic result = magicService.update("009", update);
    // Then
    assertThat(result.getId()).isEqualTo("009");
    assertThat(result.getName()).isEqualTo("update ability");
    assertThat(result.getDescription()).isEqualTo("종합적인 능력");
    verify(magicRepository, times(1)).save(update);
  }
  @Test
  void updateMagicNotFound() throws ObjectNotFoundException {
    // Given
    Magic update = new Magic();
    update.setId("009");
    update.setName("update ability");
    update.setDescription("종합적인 능력");

    given(magicRepository.findById(Mockito.anyString())).willReturn(Optional.empty());
    // When
    Throwable thrown = catchThrowable(() -> {
      Magic result = magicService.update("009", update);
    });
    // Then
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find magic with id 009");
  }
  @Test
  void deleteMagicSuccess() throws ObjectNotFoundException {
    //given
    Magic update = new Magic();
    update.setId("002");
    update.setName("hide");
    update.setDescription("숨는 능력");

    given(magicRepository.findById("002")).willReturn(Optional.of(update));
    doNothing().when(magicRepository).deleteById("002");
    // When
    magicService.delete("002");
    // then
    verify(magicRepository, times(1)).deleteById("002");
  }
  @Test
  void deleteMagicNotFound() throws ObjectNotFoundException {
    //given
    given(magicRepository.findById("002")).willReturn(Optional.empty());
    // When
    Throwable thrown = catchThrowable(() -> {
      magicService.delete("002");
    });
    // then
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find magic with id 002");
  }
}