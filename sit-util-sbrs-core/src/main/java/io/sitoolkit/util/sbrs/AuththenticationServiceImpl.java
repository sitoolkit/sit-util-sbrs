package io.sitoolkit.util.sbrs;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuththenticationServiceImpl implements AuthenticationService {

  @Autowired AuthenticationManager authenticationManager;

  @Autowired TokenProvider tokenProvider;

  @SuppressWarnings("rawtypes")
  @Autowired
  TokenConverter tokenConverter;

  @Override
  public String authenticate(String loginId, String password) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginId, password));

    @SuppressWarnings("unchecked")
    Map<String, String> ext = tokenConverter.toTokenExt(authentication.getPrincipal());

    List<String> roles = SpringSecurityUtils.toStringList(authentication.getAuthorities());

    return tokenProvider.createToken(authentication.getName(), roles, ext);
  }
}
