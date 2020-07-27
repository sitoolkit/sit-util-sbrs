package io.sitoolkit.util.sbrs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  @Autowired AuthenticationService service;

  @SuppressWarnings("rawtypes")
  @Autowired
  TokenConverter converter;

  @PostMapping("/login")
  public LoginResponseDto login(@RequestBody LoginRequestDto request) {
    String token = service.authenticate(request.getLoginId(), request.getPassword());

    return LoginResponseDto.builder().loginId(request.getLoginId()).token(token).build();
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
