package sample.domain.user;

import io.sitoolkit.util.sbrs.TmpAccountEntity;
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
public class TmpUserEntity implements TmpAccountEntity {

  @Id private String id;

  private String activateCode;
}
