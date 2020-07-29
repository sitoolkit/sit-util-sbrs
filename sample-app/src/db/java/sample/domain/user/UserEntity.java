package sample.domain.user;

import io.sitoolkit.util.sbrs.AccountEntityBase;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
public class UserEntity implements AccountEntityBase {

  @Id private String id;

  private String password;

  private String name;

  private String roles;
}
