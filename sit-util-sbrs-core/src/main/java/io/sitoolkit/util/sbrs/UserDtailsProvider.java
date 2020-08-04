package io.sitoolkit.util.sbrs;

import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDtailsProvider {

  public static UserDetails userDetails(String username, String password, List<String> roles) {
    return User.builder()
        .username(username)
        .password(password)
        .authorities(SpringSecurityUtils.toAuthrities(roles))
        .build();
  }

  public static UserDetails userDetails(String username, String password, String[] roles) {
    return userDetails(username, password, Arrays.asList(roles));
  }

  public static UserDetails userDetails(String username, List<String> roles) {
    return userDetails(username, "dummy", roles);
  }

  public static UserDetails userDetails(String username, String[] roles) {
    return userDetails(username, "dummy", roles);
  }
}
