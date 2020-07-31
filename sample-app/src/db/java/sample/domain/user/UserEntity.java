package sample.domain.user;

import io.sitoolkit.util.sbrs.LoginUser;
import io.sitoolkit.util.sbrs.SpringSecurityUtils;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
public class UserEntity extends LoginUser {

  private static final long serialVersionUID = 1L;

  @Id private String id;

  private String password;

  private String name;

  private String roles;

  @Override
  public String getUsername() {
    return StringUtils.isEmpty(super.getUsername()) ? this.id : super.getUsername();
  }

  @Override
  public List<GrantedAuthority> getAuthorities() {
    return (Objects.isNull(super.getAuthorities()) || super.getAuthorities().isEmpty())
        ? SpringSecurityUtils.toAuthrities(StringUtils.splitByWholeSeparator(this.roles, ","))
        : super.getAuthorities();
  }
}
