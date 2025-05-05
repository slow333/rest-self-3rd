package com.magic.user;

import com.magic.system.exception.ObjectNotFoundException;
import com.magic.system.exception.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public SiteUser findByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
  }

  public List<SiteUser> findAll() {
    return userRepository.findAll();
  }

  public SiteUser createUser(SiteUser siteUser) {
    return userRepository.save(siteUser);
  }

  public SiteUserDto updateUser(Long userId, SiteUserDto update) throws ObjectNotFoundException {
    SiteUser oldUser = userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    oldUser.setUsername(update.username());
    oldUser.setEnabled(update.enabled());
    oldUser.setRoles(update.roles());

    SiteUser updatedSiteUser = userRepository.save(oldUser);
    SiteUserDto dto = new ToSiteUserDto().convert(updatedSiteUser);
    return dto;
  }

  public void deleteUser(Long userId) throws ObjectNotFoundException {
    userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    userRepository.deleteById(userId);
  }

  public SiteUser findById(Long userId) throws ObjectNotFoundException {
    SiteUser siteUser = userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    return siteUser;
  }
}
