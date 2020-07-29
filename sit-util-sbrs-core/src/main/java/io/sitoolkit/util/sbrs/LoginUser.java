package io.sitoolkit.util.sbrs;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

@EqualsAndHashCode(callSuper = false)
public class LoginUser extends User {

  /** */
  private static final long serialVersionUID = 1L;

  @Getter @Setter private String name;

  public LoginUser(String loginId, String password, String name, String... roles) {
    super(loginId, password, SpringSecurityUtils.toAuthrities(roles));

    this.name = name;
  }

  public LoginUser(String loginId, String name, List<String> roles) {
    this(loginId, "dummy", name, roles.toArray(new String[roles.size()]));
  }

  public String getLoginId() {
    return getUsername();
  }
}
