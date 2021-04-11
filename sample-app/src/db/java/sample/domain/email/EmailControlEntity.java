package sample.domain.email;

import io.sitoolkit.util.sbrs.EmailEntity;
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
public class EmailControlEntity implements EmailEntity {

  @Id private String id;

  private String to;

  private String subject;

  private String message;

  private String sendStatus;

  private String mimeType;
}