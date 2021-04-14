package sample.domain.email;

import io.sitoolkit.util.sbrs.EmailEntity;
import javax.persistence.Column;
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

  @Column(name="\"from\"")
  private String from;

  private String to;

  private String subject;

  private String textMessage;

  @Column(length=1000)
  private String htmlMessage;

  private String mimeType;

  private String sendStatus;
}