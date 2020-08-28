package io.sitoolkit.util.sbrs;

import java.util.Map;

public interface Notifier {
  void activateCodeNotify(
      String loginId, String to, String activateCode, Map<String, String> notifyParams);
}
