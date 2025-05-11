package com.magic.user;

import com.magic.client.redis.RedisCacheClient;
import com.magic.system.exception.ObjectNotFoundException;
import com.magic.system.exception.PasswordChangeIllegalArgException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
  private final RedisCacheClient redisCacheClient;

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
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // If the user not admin
    if(auth.getAuthorities().stream()
            .noneMatch(a -> a.getAuthority().equals("ROLE_admin"))) {
      oldUser.setUsername(update.getUsername());
    } else {
      oldUser.setUsername(update.getUsername());
      oldUser.setEnabled(update.isEnabled());
      oldUser.setRoles(update.getRoles());
      redisCacheClient.delete("whiteList:" + userId);
    }
    return userRepository.save(oldUser);
  }

  public void deleteUser(Long userId) throws ObjectNotFoundException {
    userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    redisCacheClient.delete("whiteList:" + userId);
    userRepository.deleteById(userId);
  }

  @Override
  public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException{
    return userRepository.findByUsername(username)
            .map(MyUserPrincipal::new)
            .orElseThrow(() -> new UsernameNotFoundException("The username " + username + " is not found."));
  }

  public void changePassword(Long userId, String oldPassword,
                             String newPassword, String confirmPassword) throws ObjectNotFoundException {
    SiteUser su = userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("user", userId));

    if(!passwordEncoder.matches(oldPassword, su.getPassword())) {
      throw new BadCredentialsException("Old password is incorrect.");
    }

    if(!newPassword.equals(confirmPassword)) {
      throw new PasswordChangeIllegalArgException("Confirm password is not correct.");
    }

    String passwordPolicy = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$";
    if(!newPassword.matches(passwordPolicy)) {
      throw new PasswordChangeIllegalArgException("Password policy not confirm.");
    }
    redisCacheClient.delete("whiteList:" + userId);
    su.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(su);
  }
}
