package org.Internship.HpsApplication.controller;

import java.net.URI;
import java.util.List;
import org.Internship.HpsApplication.Utility.RoleUserForm;
import org.Internship.HpsApplication.domain.AppUser;
import org.Internship.HpsApplication.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserAPI {

  private AccountService accountService;

  public UserAPI(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/users")
  @PostAuthorize("hasAuthority('USER')")
  public ResponseEntity<List<AppUser>> appUsers() {
    return ResponseEntity.ok(accountService.listUsers());
  }

  @PostMapping("/user/save")
  @PostAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<AppUser> saveUser (@RequestBody AppUser appUser){
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
    return ResponseEntity.created(uri).body(accountService.saveAppUser(appUser));
  }

  @PostMapping("/addRoleToUser")
  @PostAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> addRoleToUser(@RequestBody RoleUserForm form){
    accountService.addRoleToUser(form.getUsername(), form.getRoleName());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/delete/user/{username}")
  @PostAuthorize(("hasAuthority('SUPERADMIN')"))
  public ResponseEntity<?> RemoveUser(@PathVariable() String username){
    accountService.removeUser(username);
    return ResponseEntity.noContent().build();
  }

}
