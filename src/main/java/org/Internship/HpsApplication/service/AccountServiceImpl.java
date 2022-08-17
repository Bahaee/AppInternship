package org.Internship.HpsApplication.service;

import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.Internship.HpsApplication.Utility.Utility;
import org.Internship.HpsApplication.domain.AppGroup;
import org.Internship.HpsApplication.domain.AppRole;
import org.Internship.HpsApplication.domain.AppUser;
import org.Internship.HpsApplication.repository.AppGroupRepository;
import org.Internship.HpsApplication.repository.AppRoleRepository;
import org.Internship.HpsApplication.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {


  private AppUserRepository appUserRepository;
  private AppRoleRepository appRoleRepository;
  private AppGroupRepository appGroupRepository;
  private PasswordEncoder passwordEncoder;

  public AccountServiceImpl(AppUserRepository appUserRepository,
      AppRoleRepository appRoleRepository, AppGroupRepository appGroupRepository, PasswordEncoder passwordEncoder) {
    this.appUserRepository = appUserRepository;
    this.appRoleRepository = appRoleRepository;
    this.appGroupRepository = appGroupRepository;
    this.passwordEncoder = passwordEncoder;
  }


  @Override
  public AppUser saveAppUser(AppUser appUser) {
    String pw = appUser.getPassword();
    Long id = appUser.getId();
    appUser.setPassword(passwordEncoder.encode(pw));
    log.info("Saving user {} to our DB : ", appUser.getUsername());
    appUser.setId(id);
    return appUserRepository.save(appUser);
  }

  public AppUser addAppUser(String username){
    log.info("Saving new user {} to the database",username);
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    return (AppUser) Utility.appendLdapGroupsToUsers(appUserRepository.findAll());  }

  @Override
  public AppRole saveAppRole(AppRole appRole) {
    log.info("Saving role {} to our DB : ", appRole.getRoleName());
    return appRoleRepository.save(appRole);
  }

  @Override
  public AppGroup saveAppGroup(AppGroup appGroup) {
    log.info("Saving group {} to our DB : ", appGroup.getGroupName());
    return appGroupRepository.save(appGroup);
  }

  @Override
  public void addRoleToUser(String username, String roleName) {
    AppUser appUser = appUserRepository.findByUsername(username);
    AppRole appRole = appRoleRepository.findByRoleName(roleName);
    log.info("Adding role {} to the user {}.",roleName,username);
    appUser.getRoles().add(appRole);
  }

  @Override
  public void addUserToGroup(String username, String groupName) {
    AppUser appUser = appUserRepository.findByUsername(username);
    AppGroup appGroup = appGroupRepository.findByGroupName(groupName);
    log.info("Adding role {} to the user {}.",username,groupName);
    appGroup.getUsers().add(appUser);
  }

  @Override
  public AppUser loadUserByUsername(String username) {
    log.info("Fetching user {} into the database.",username);
    return appUserRepository.findByUsername(username);
  }

  @Override
  public AppGroup getGroupByGroupName(String groupName) {
    log.info("Fetching group {} into the database.",groupName);
    return appGroupRepository.findByGroupName(groupName);
  }

  @Override
  public List<AppUser> listUsers() {
    log.info("Fetching All the users.");
    return appUserRepository.findAll();
  }

  @Override
  public List<AppRole> listRoles() {
    log.info("Fetching All the roles.");
    return appRoleRepository.findAll();
  }

  @Override
  public List<AppGroup> listGroups() {
    log.info("Fetching All the groups.");
    return appGroupRepository.findAll();
  }

  @Override
  public void removeUser(String username) {
    log.info("Deleting user{} from the database.",username);
    appUserRepository.deleteByUsername(username);
  }

  @Override
  public void removeGroup(String groupName) {
    log.info("Deleting group {} from the database.",groupName);
    appGroupRepository.deleteByGroupName(groupName);
  }
}
