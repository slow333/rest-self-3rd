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

  public SiteUser updateUser(Long userId, SiteUser update) throws ObjectNotFoundException {
    SiteUser oldUser = userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    oldUser.setUsername(update.getUsername());
    oldUser.setEnabled(update.isEnabled());
    oldUser.setRoles(update.getRoles());

    SiteUser updatedSiteUser = userRepository.save(oldUser);
    return updatedSiteUser;
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
