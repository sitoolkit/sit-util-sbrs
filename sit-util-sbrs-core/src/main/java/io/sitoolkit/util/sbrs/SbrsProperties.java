package io.sitoolkit.util.sbrs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "sit.sbrs")
public class SbrsProperties {

  private String registoryType = "db";
}
