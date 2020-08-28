package io.sitoolkit.util.sbrs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sit.sbrs.mail.notification")
public class SbrsNotificationProperties {
  private String from;
  private String activateSubject;
  private String templateDir;
}
