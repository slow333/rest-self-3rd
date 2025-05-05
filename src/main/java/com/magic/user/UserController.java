package com.magic.user;

import com.magic.system.Result;
import com.magic.system.StatusCode;
import com.magic.system.exception.ObjectNotFoundException;
import com.magic.system.exception.UsernameNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.baseUrl}/users")
public class UserController {

  private final UserService userService;

  @GetMapping("/{username}")
  public Result findByUsername(@PathVariable String username) throws UsernameNotFoundException {
    SiteUser user = userService.findByUsername(username);
    SiteUserDto dto = new ToSiteUserDto().convert(user);
    return new Result(true, StatusCode.SUCCESS, "Find User Success.", dto);
  }

  @GetMapping
  public Result findAll(){
    List<SiteUser> siteUsers = userService.findAll();
    List<SiteUserDto> dtos = siteUsers.stream().map(new ToSiteUserDto()::convert).toList();
    return new Result(true, StatusCode.SUCCESS, "Find All Success.", dtos);
  }

  @PostMapping
  public Result createUser(@Valid @RequestBody SiteUser siteUser){
    SiteUser su = userService.createUser(siteUser);
    SiteUserDto dto = new ToSiteUserDto().convert(su);
    return new Result(true, StatusCode.SUCCESS, "Create User Success.", dto);
  }

  @PutMapping("/{userId}")
  public Result updateUser(
          @PathVariable Long userId,
          @Valid @RequestBody SiteUserDto dto) throws ObjectNotFoundException {
    SiteUserDto suDto = userService.updateUser(userId, dto);
    return new Result(true, StatusCode.SUCCESS, "Update User Success.", suDto);
  }

  @DeleteMapping("/{userId}")
  public Result deleteUser(@PathVariable Long userId) throws ObjectNotFoundException {
    userService.deleteUser(userId);
    return new Result(true, StatusCode.SUCCESS, "Delete User Success.");
  }
}
