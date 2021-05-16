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
public class CreateRequestDto {
  private String loginId;
  private String notifyTo;
  private Map<String, String> ext;
}
