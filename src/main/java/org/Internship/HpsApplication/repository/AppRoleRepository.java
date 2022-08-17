package org.Internship.HpsApplication.repository;

import org.Internship.HpsApplication.domain.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {

  AppRole findByRoleName(String roleName);

}
