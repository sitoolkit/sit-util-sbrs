package io.sitoolkit.util.sbrs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sit.sbrs.mail.activate")
public class SbrsNotifyActivateProperties {
  private String from;
  private String sub;
  private String htmlTemplate;
  private String textTemplate;
}
