package io.sitoolkit.util.sbrs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sit.sbrs")
public class SbrsProperties {

  private String registoryType = "db";
  private String notifyType;
  private String changePasswordUrl;
}
