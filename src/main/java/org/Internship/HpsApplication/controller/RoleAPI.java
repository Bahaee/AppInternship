package org.Internship.HpsApplication.controller;

import java.net.URI;
import java.util.List;
import org.Internship.HpsApplication.domain.AppRole;
import org.Internship.HpsApplication.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class RoleAPI {

  private AccountService accountService;

  public RoleAPI(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/roles")
  @PostAuthorize("hasAuthority('USER')")
  public ResponseEntity<List<AppRole>> appRoles(){
    return ResponseEntity.ok(accountService.listRoles());
  }

  @PostMapping("/role/save")
  @PostAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<AppRole> saveRole(@RequestBody AppRole appRole){
    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/role/save").toUriString());
    return  ResponseEntity.created(uri).body(accountService.saveAppRole(appRole));
  }
}


