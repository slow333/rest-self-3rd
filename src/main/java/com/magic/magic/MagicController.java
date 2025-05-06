package com.magic.magic;

import com.magic.system.Result;
import com.magic.system.StatusCode;
import com.magic.system.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.endpoint.baseUrl}/magics")
public class MagicController {

  private final MagicService magicService;
  private final ToMagicDto toMagicDto;
  private final ToMagicEntity toMagicEntity;

  public MagicController(MagicService magicService, ToMagicDto toMagicDto, ToMagicEntity toMagicEntity) {
    this.magicService = magicService;
    this.toMagicDto = toMagicDto;
    this.toMagicEntity = toMagicEntity;
  }

  @GetMapping("/{magicId}")
  public Result findMagicById(@PathVariable String magicId) throws ObjectNotFoundException {
    Magic magic = magicService.findById(magicId);
    MagicDto magicDto = toMagicDto.convert(magic);
    return new Result(true, StatusCode.SUCCESS, "Find One Success.", magicDto);
  }

  @GetMapping // 기본 적으로 findAll() page 없는 것을 포함함
  public Result findAll(Pageable pageable) {
    Page<Magic> magicsPage = magicService.findAll(pageable);
    Page<MagicDto> magicDtosPage = magicsPage.map(toMagicDto::convert);
    return new Result(true, StatusCode.SUCCESS, "Find All Success.", magicDtosPage);
  }
//  @GetMapping
//  public Result findAll() {
//    List<Magic> magicsPage = magicService.findAll();
//    List<MagicDto> magicDtosPage = magicsPage.stream()
//            .map(toMagicDto::convert).toList();
//    return new Result(true, StatusCode.SUCCESS, "Find All Success.", magicDtosPage);
//  }

  @PostMapping
  public Result addMagic(@Valid @RequestBody MagicDto magicDto) {
    Magic magic = toMagicEntity.convert(magicDto);
    Magic newMagic = magicService.add(magic);
    MagicDto magicDtoSaved = toMagicDto.convert(newMagic);

    return new Result(true, StatusCode.SUCCESS, "Add Magic Success.", magicDtoSaved);
  }

  @PutMapping("/{magicId}")
  public Result updateMagic(@PathVariable String magicId,
                            @Valid @RequestBody MagicDto magicDto) throws ObjectNotFoundException {
    Magic convertedMagic = toMagicEntity.convert(magicDto);
    Magic update = magicService.update(magicId, convertedMagic);
    MagicDto dto = toMagicDto.convert(update);

    return new Result(true, StatusCode.SUCCESS, "Update Magic Success.", dto);
  }

  @DeleteMapping("/{magicId}")
  public Result deleteMagic(@PathVariable String magicId) throws ObjectNotFoundException {
    magicService.delete(magicId);
    return new Result(true, StatusCode.SUCCESS, "Delete Magic Success.");
  }

  @PostMapping("/search")
  public Result findMagicByKeyword(@RequestBody Map<String, String> query, Pageable pageable) {
    Page<Magic> magicsPage = magicService.findByQuery(query, pageable);
    Page<MagicDto> magicDtosPage = magicsPage.map(toMagicDto::convert);
    return new Result(true, StatusCode.SUCCESS, "Find Magic By Query Success.", magicDtosPage);
  }
}
