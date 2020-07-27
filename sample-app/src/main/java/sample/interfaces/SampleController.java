package sample.interfaces;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SampleController {

  @GetMapping("/user")
  public String user(@AuthenticationPrincipal UserDetails loginUser) {
    log.info("loginUser:{}", ToStringBuilder.reflectionToString(loginUser));
    return "user";
  }

  @GetMapping("/admin")
  public String admin() {
    return "admin";
  }
}
