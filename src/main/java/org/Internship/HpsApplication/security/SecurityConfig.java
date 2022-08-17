package org.Internship.HpsApplication.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import org.Internship.HpsApplication.filters.JwtAuthenticationFilter;
import org.Internship.HpsApplication.filters.JwtAuthorizationFilter;
import org.Internship.HpsApplication.service.AccountService;
import org.Internship.HpsApplication.service.UserDeatailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private AccountService accountService;
  private UserDeatailsServiceImp userDeatailsServiceImp;

  public SecurityConfig(AccountService accountService,
      UserDeatailsServiceImp userDeatailsServiceImp) {
    this.accountService = accountService;
    this.userDeatailsServiceImp = userDeatailsServiceImp;
  }


 /* @Override //Users that have Access
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDeatailsServiceImp);
  }*/

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .ldapAuthentication()
        .ldapAuthoritiesPopulator(ldapAuthoritiesPopulator())
        .userDnPatterns("cn={0},ou=users,dc=example,dc=com")
        .contextSource()
        .url("ldap://localhost:10389")
        .and()
        .passwordCompare()
        .passwordEncoder(new LdapShaPasswordEncoder())
        .passwordAttribute("userPassword");
  }

  @Override //Spécifer les droits d'acces
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.headers().frameOptions().disable();
    http.authorizeHttpRequests().antMatchers("/refreshToken/**").permitAll();
    //http.formLogin();
    //http.authorizeHttpRequests().antMatchers(HttpMethod.GET,"/users/**").hasAuthority("USER");
    //http.authorizeHttpRequests().antMatchers(HttpMethod.POST,"/user/**").hasAuthority("ADMIN");
    http.authorizeHttpRequests().anyRequest().authenticated();
    http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
    http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


  private LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
    return new LdapAuthoritiesPopulator() {
      @Override
      public Collection<? extends GrantedAuthority> getGrantedAuthorities(
          DirContextOperations userData,
          String username) {

        LinkedList<SimpleGrantedAuthority> res = new LinkedList();
        Arrays.stream(userData.getObjectAttributes("memberOf"))
            .iterator()
            .forEachRemaining(m -> {
              if (m.toString().equals("GROUP_USERS"))
                res.add(new SimpleGrantedAuthority("USER"));
              if (m.toString().equals("GROUP_ADMIN"))
                res.add(new SimpleGrantedAuthority("ADMIN"));
              if (m.toString().equals("GROUP_SUPER_ADMIN"))
                res.add(new SimpleGrantedAuthority("SUPER_ADMIN"));
            });
        if (res.isEmpty())
          return Arrays.asList(new SimpleGrantedAuthority("USER"));
        else
          return res;
      }
    };

  }
}
