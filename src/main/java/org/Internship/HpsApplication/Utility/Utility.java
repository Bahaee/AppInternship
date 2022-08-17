package org.Internship.HpsApplication.Utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import org.Internship.HpsApplication.domain.AppUser;
import org.Internship.HpsApplication.service.LdapAccountService;


public class Utility {
  private static LdapAccountService ldapService = new LdapAccountService();
  public static List<Object> appendLdapGroupsToUsers(List<AppUser> users){
    List<Object> newUsers = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    users.forEach(user-> {
      try {
        Map<String, Object> map = objectMapper.convertValue(user, Map.class);
        map.put("ldapGroups",ldapService.getAllGroups(user.getUsername()));
        newUsers.add(map);
      } catch (NamingException e) {
        throw new RuntimeException(e);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return newUsers;
  }
}
