package io.sitoolkit.util.sbrs;

import java.util.List;
import java.util.Map;

public interface TokenProvider {

  String createToken(String loginId, List<String> roles, Map<String, String> ext);
}
