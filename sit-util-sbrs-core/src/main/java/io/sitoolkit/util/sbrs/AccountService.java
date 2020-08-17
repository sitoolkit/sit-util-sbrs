package io.sitoolkit.util.sbrs;

import java.util.Map;

public interface AccountService {
  boolean create(String loginId, String password, Map<String, String> ext);
}
