package io.sitoolkit.util.sbrs;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivateRequestDto {
  private String loginId;
  private String activateCode;
  private String password;
  private Map<String, String> ext;
}
