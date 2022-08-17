package org.Internship.HpsApplication.service;

import java.util.ArrayList;
import java.util.Collection;
import org.Internship.HpsApplication.domain.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDeatailsServiceImp implements UserDetailsService {

  private AccountService accountService;

  public UserDeatailsServiceImp(AccountService accountService) {
    this.accountService = accountService;
  }


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser appUser = accountService.loadUserByUsername(username);
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    appUser.getRoles().forEach(r->{authorities.add(new SimpleGrantedAuthority(r.getRoleName()));});
    return new User(appUser.getUsername(),appUser.getPassword(),authorities);
  }
}


