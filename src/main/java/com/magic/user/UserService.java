package com.magic.user;

import com.magic.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public List<SiteUser> findAll() {
    return userRepository.findAll();
  }

  public SiteUser findById(Long userId) throws ObjectNotFoundException {
    return userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("user", userId));
  }

  public SiteUser createUser(SiteUser siteUser) {
    siteUser.setPassword(passwordEncoder.encode(siteUser.getPassword()));
    return userRepository.save(siteUser);
  }

  public SiteUser updateUser(Long userId, SiteUser update) throws ObjectNotFoundException {
    SiteUser oldUser = userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    oldUser.setUsername(update.getUsername());
    oldUser.setEnabled(update.isEnabled());
    oldUser.setRoles(update.getRoles());

    return userRepository.save(oldUser);
  }

  public void deleteUser(Long userId) throws ObjectNotFoundException {
    userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    userRepository.deleteById(userId);
  }

  @Override
  public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException{
    return userRepository.findByUsername(username)
            .map(MyUserPrincipal::new)
            .orElseThrow(() -> new UsernameNotFoundException("The username " + username + " is not found."));
  }
}
