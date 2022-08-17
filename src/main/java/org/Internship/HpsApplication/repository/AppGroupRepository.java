package org.Internship.HpsApplication.repository;

import org.Internship.HpsApplication.domain.AppGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppGroupRepository extends JpaRepository<AppGroup,String> {

  AppGroup findByGroupName(String groupName);
  void deleteByGroupName(String groupName);

}
