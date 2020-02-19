package io.sitoolkit.util.sbrs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

  @Autowired AuthenticationService service;

  @SuppressWarnings("rawtypes")
  @Autowired
  TokenConverter converter;

  @PostMapping("/login")
  public LoginResponseDto login(@RequestBody LoginRequestDto request) {
    LoginResponseDto response = LoginResponseDto.builder().loginId(request.getLoginId()).build();

    try {
      String token = service.authenticate(request.getLoginId(), request.getPassword());
      response.setToken(token);
      response.setSuccess(true);
    } catch (AuthenticationException e) {
      log.debug("Login failure", e);
      response.setSuccess(false);
    }

    return response;
  }

  @SuppressWarnings("unchecked")
  @GetMapping("/me")
  public CurrentUserResponseDto currentUser(@AuthenticationPrincipal UserDetails userDetails) {

    if (userDetails == null) {
      throw new BadCredentialsException("No token request");
    }

    List<String> roles = SpringSecurityUtils.toStringList(userDetails.getAuthorities());

    return CurrentUserResponseDto.builder()
        .loginId(userDetails.getUsername())
        .roles(roles)
        .ext(converter.toTokenExt(userDetails))
        .build();
  }
}
