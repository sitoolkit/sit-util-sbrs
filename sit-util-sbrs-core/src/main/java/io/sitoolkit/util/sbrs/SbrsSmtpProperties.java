package io.sitoolkit.util.sbrs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sit.sbrs.mail")
public class SbrsSmtpProperties {
  private String smtpUser;
  private String smtpPassword;
  private String smtpHost;
  private Integer smtpPort;
}
