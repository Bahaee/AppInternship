package org.Internship.HpsApplication.controller;

import java.net.URI;
import java.util.List;
import org.Internship.HpsApplication.Utility.UserGroupForm;
import org.Internship.HpsApplication.domain.AppGroup;
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
public class GroupAPI {

  private AccountService accountService;

  public GroupAPI(AccountService accountService) {
    this.accountService = accountService;
  }


  @GetMapping("/groups")
  @PostAuthorize("hasAuthority('USER')")
  public ResponseEntity<List<AppGroup>> appGroups(){
    return ResponseEntity.ok(accountService.listGroups());
  }

  @PostMapping("/group/save")
  @PostAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<AppGroup> saveGroup(@RequestBody AppGroup  appGroup){
    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/group/save").toUriString());
    return  ResponseEntity.created(uri).body(accountService.saveAppGroup(appGroup));
  }


  @PostMapping("/addUserToGroup")
  @PostAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> addRoleToUser(@RequestBody UserGroupForm form){
    accountService.addRoleToUser(form.getUsername(), form.getGroupName());
    return ResponseEntity.ok().build();
  }


  @DeleteMapping("/delete/group/{groupName}")
  @PostAuthorize(("hasAuthority('SUPERADMIN')"))
  public ResponseEntity<?> RemoveGroup(@PathVariable() String groupName){
    accountService.removeGroup(groupName);
    return ResponseEntity.noContent().build();
  }




}

