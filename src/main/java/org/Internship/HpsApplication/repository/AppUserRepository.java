package org.Internship.HpsApplication.repository;

import org.Internship.HpsApplication.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {

  AppUser findByUsername(String username);
  void deleteByUsername(String username);


}
