package io.sitoolkit.util.sbrs;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserResponseDto {
  private String loginId;
  private List<String> roles;
  private Map<String, String> ext;
}
