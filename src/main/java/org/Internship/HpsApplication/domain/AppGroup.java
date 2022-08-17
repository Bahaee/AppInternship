package org.Internship.HpsApplication.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Group")
@Data @NoArgsConstructor @AllArgsConstructor
public class AppGroup implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String groupName;

  @ManyToMany
  @JoinTable(name = "users_group",joinColumns = @JoinColumn(name = "GroupName"),
      inverseJoinColumns = @JoinColumn(name = "userId"))
  private Collection<AppUser> users = new ArrayList<>();

  @ManyToMany
  @JoinTable(name = "roles_group",joinColumns = @JoinColumn(name = "GroupName"),
      inverseJoinColumns = @JoinColumn(name = "roleId"))
  private Collection<AppUser> roles = new ArrayList<>();

}
