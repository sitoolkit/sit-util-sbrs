package io.sitoolkit.util.sbrs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivateResponseDto {
  private String loginId;
  private boolean success;
}
