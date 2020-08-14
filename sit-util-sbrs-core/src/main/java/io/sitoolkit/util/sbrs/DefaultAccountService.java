package io.sitoolkit.util.sbrs;

import java.util.Map;

public interface DefaultAccountService {
  boolean create(String loginId, String password, Map<String, String> ext);
}
