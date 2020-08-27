package io.sitoolkit.util.sbrs;

import java.util.Map;

public interface AcitvateCodeNotifier {
  void notify(String loginId, String to, String activateCode, Map<String, String> notifyParams);
}
