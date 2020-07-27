package io.sitoolkit.util.sbrs;

import java.util.List;
import java.util.Map;

public interface TokenConverter<T> {

  Map<String, String> toTokenExt(T principal);

  T toPrincipal(String loginId, List<String> roles, Map<String, String> ext);
}
