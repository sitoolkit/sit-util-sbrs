package io.sitoolkit.util.sbrs;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

  /** */
  private static final long serialVersionUID = 1L;

  private String username;
  private String password;
  private List<GrantedAuthority> authorities;
  private boolean accountNonExpired = true;
  private boolean accountNonLocked = true;
  private boolean credentialsNonExpired = true;
  private boolean enabled = true;

  private String name;

  public LoginUser(String loginId, String password, String name, String... roles) {
    this.username = loginId;
    this.password = password;
    this.authorities = SpringSecurityUtils.toAuthrities(roles);
    this.name = name;
  }

  public LoginUser(String loginId, String name, List<String> roles) {
    this(loginId, "dummy", name, roles.toArray(new String[roles.size()]));
  }

  public String getLoginId() {
    return getUsername();
  }
}
