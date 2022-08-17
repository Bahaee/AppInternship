package org.Internship.HpsApplication.service;

import java.util.List;
import org.Internship.HpsApplication.domain.AppGroup;
import org.Internship.HpsApplication.domain.AppRole;
import org.Internship.HpsApplication.domain.AppUser;
import org.springframework.security.core.userdetails.User;

public interface AccountService {
  public AppUser addAppUser(String username);

  AppUser saveAppUser(AppUser appUser);
  AppRole saveAppRole(AppRole appRole);
  AppGroup saveAppGroup(AppGroup appGroup);
  void addRoleToUser(String username,String roleName);
  void addUserToGroup(String username,String groupName);

  AppUser loadUserByUsername(String username);
  AppGroup getGroupByGroupName(String groupName);
  List<AppUser> listUsers();
  List<AppRole> listRoles();
  List<AppGroup> listGroups();

  void removeUser(String username);
  void removeGroup(String groupName);

}
