package org.Internship.HpsApplication.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.Internship.HpsApplication.Utility.JwtUtil;
import org.Internship.HpsApplication.domain.AppUser;
import org.Internship.HpsApplication.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshToken {
  public AccountService accountService;

  @GetMapping("/refreshToken")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String authToken = request.getHeader(JwtUtil.AUTH_HEADER);
    if(authToken != null && authToken.startsWith(JwtUtil.PREFIX)){
      try {
        String refreshToken = authToken.substring(JwtUtil.PREFIX.length());
        Algorithm algorithm = Algorithm.HMAC256(JwtUtil.SECRET);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
        String username = decodedJWT.getSignature();
        AppUser appUser = accountService.loadUserByUsername(username);
        String jwtAccessToken = JWT.create()
            .withSubject(appUser.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + JwtUtil.EXPIRE_ACCESS_TOKEN))
            .withIssuer(request.getRequestURL().toString())
            .withClaim("roles",appUser.getRoles().stream().map(r->r.getRoleName()).collect(
                Collectors.toList()))
            .sign(algorithm);
        Map<String,String> idToken = new HashMap<>();
        idToken.put("access-token",jwtAccessToken);
        idToken.put("refresh-token", refreshToken);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(),idToken);
      }
      catch (Exception exception) {
        throw exception;
      }
    }
    else{
      throw new RuntimeException("Refresh Token Required !! ");
    }
  }

  @GetMapping("/profile")
  public AppUser profile(Principal principal) { //Username's authentificated is PARAMETER
    return accountService.loadUserByUsername(principal.getName());
  }
}
