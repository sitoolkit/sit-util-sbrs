package sample.infrastructure.auth;

import java.util.List;

import org.springframework.security.core.userdetails.User;

import io.sitoolkit.util.sbrs.SpringSecurityUtils;
import lombok.Getter;
import lombok.Setter;

public class SampleUser extends User {

  /** */
  private static final long serialVersionUID = 1L;

  @Getter @Setter private String name;

  public SampleUser(String loginId, String password, String name, String... roles) {
    super(loginId, password, SpringSecurityUtils.toAuthrities(roles));

    this.name = name;
  }

  public SampleUser(String loginId, String name, List<String> roles) {
    this(loginId, "dummy", name, roles.toArray(new String[roles.size()]));
  }

  public String getLoginId() {
    return getUsername();
  }
}
