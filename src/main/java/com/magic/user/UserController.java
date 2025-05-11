package com.magic.user;

import com.magic.system.Result;
import com.magic.system.StatusCode;
import com.magic.system.exception.ObjectNotFoundException;
import com.magic.user.converter.ToSiteUserDto;
import com.magic.user.converter.ToSiteUserEntity;
import com.magic.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.baseUrl}/users")
public class UserController {

  private final UserService userService;
  private final ToSiteUserDto toSiteUserDto;
  private final ToSiteUserEntity toSiteUserEntity;

  @GetMapping
  public Result findAll(){
    List<SiteUser> siteUsers = userService.findAll();
    List<UserDto> dtos = siteUsers.stream().map(toSiteUserDto::convert).toList();
    return new Result(true, StatusCode.SUCCESS, "Find All Success.", dtos);
  }
  @GetMapping("/{userId}")
  public Result findUserById(@PathVariable Long userId) throws ObjectNotFoundException {
    SiteUser su = userService.findById(userId);
    UserDto suDto = toSiteUserDto.convert(su);
    return new Result(true, StatusCode.SUCCESS, "Find User Success.", suDto);
  }

  @PutMapping("/{userId}")
  public Result updateUser(
          @PathVariable Long userId,
          @Valid @RequestBody UserDto userDto) throws ObjectNotFoundException {
    SiteUser update = toSiteUserEntity.convert(userDto);
    SiteUser su = userService.updateUser(userId, update);
    UserDto suDto = toSiteUserDto.convert(su);

    return new Result(true, StatusCode.SUCCESS, "Update User Success.", suDto);
  }

  @PostMapping
  public Result createUser(@Valid @RequestBody SiteUser siteUser){
    SiteUser su = userService.createUser(siteUser);
    UserDto dto = toSiteUserDto.convert(su);
    return new Result(true, StatusCode.SUCCESS, "Create User Success.", dto);
  }

  @DeleteMapping("/{userId}")
  public Result deleteUser(@PathVariable Long userId) throws ObjectNotFoundException {
    userService.deleteUser(userId);
    return new Result(true, StatusCode.SUCCESS, "Delete User Success.");
  }

  @PatchMapping("/{userId}/password")
  public Result changePassword(@PathVariable Long userId,
                               @RequestBody Map<String, String> passwordMap) throws ObjectNotFoundException {
    String oldPassword = passwordMap.get("oldPassword");
    String newPassword = passwordMap.get("newPassword");
    String confirmPassword = passwordMap.get("confirmPassword");

    userService.changePassword(userId, oldPassword, newPassword, confirmPassword);
    return new Result(true, StatusCode.SUCCESS, "Change Password Success.", null);

  }
}
