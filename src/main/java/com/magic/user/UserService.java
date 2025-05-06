package com.magic.user;

import com.magic.security.MyUserPrincipal;
import com.magic.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public List<SiteUser> findAll() {
    return userRepository.findAll();
  }

  public SiteUser createUser(SiteUser siteUser) {
    siteUser.setPassword(passwordEncoder.encode(siteUser.getPassword()));
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

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userRepository.findByUsername(username)
            .map(MyUserPrincipal::new)
            .orElseThrow(() -> new UsernameNotFoundException("The username " + username + " is not found."));
  }
}
