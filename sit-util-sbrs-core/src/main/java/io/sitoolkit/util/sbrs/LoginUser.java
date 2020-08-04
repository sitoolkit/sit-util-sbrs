package io.sitoolkit.util.sbrs;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

@EqualsAndHashCode(callSuper = false)
public class LoginUser<T extends AccountEntity> extends User {

  /** */
  private static final long serialVersionUID = 1L;

  @Getter @Setter private transient T entity;

  public LoginUser(String loginId, String password, T entity, String... roles) {
    super(loginId, password, SpringSecurityUtils.toAuthrities(roles));
    this.entity = entity;
  }

  public LoginUser(String loginId, T entity, List<String> roles) {
    this(loginId, "dummy", entity, roles.toArray(new String[roles.size()]));
  }

  public String getLoginId() {
    return getUsername();
  }
}
