package org.Internship.HpsApplication.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import net.minidev.json.JSONObject;

public class LdapAccountService {
  DirContext connexion;

  public void newConnexion()throws Exception{
  Properties environment = new Properties();
  environment.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.ldapCtxFactory");
  environment.put(Context.PROVIDER_URL, "ldap://localhost:10389");
  environment.put(Context.SECURITY_PRINCIPAL,"uid=admin,ou=system");
  environment.put(Context.SECURITY_CREDENTIALS,"secret");
    try{
    connexion = new InitialDirContext(environment);
    }
    catch (AuthenticationException authenticationException){
      System.out.println(authenticationException.getMessage());
    }
    catch (NamingException namingException){
      namingException.printStackTrace();
    }
  }

  public List<JSONObject> getAllGroups(String username) throws Exception {
    newConnexion();
    String searchFilter = "(objectClass=groupOfUniqueNames)";
    String[] reqAtt = { "cn", "uniqueMember" };
    SearchControls controls = new SearchControls();
    controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    controls.setReturningAttributes(reqAtt);

    NamingEnumeration users = connexion.search("ou=groups,dc=example,dc=com", searchFilter, controls);
    List<JSONObject> groups = new ArrayList<>();
    SearchResult result;
    while (users.hasMore()) {
      result = (SearchResult) users.next();
      Attributes attr = result.getAttributes();
      if(attr.get("uniqueMember").toString().contains(username)){
        JSONObject json = new JSONObject();
        json.put("name",attr.get("cn").toString().split("cn:")[1].replaceAll("\\s",""));
        groups.add(json);
      }
    }
    connexion.close();

    return groups;
  }
}
