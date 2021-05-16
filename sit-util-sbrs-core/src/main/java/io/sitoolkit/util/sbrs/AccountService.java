package io.sitoolkit.util.sbrs;

import java.util.Map;

public interface AccountService {
  boolean create(String loginId, String notifyTo, Map<String, String> ext);

  boolean activate(String loginId, String activateCode, String password, Map<String, String> ext);

  boolean resetPassword(String notifyTo, Map<String, String> ext);

  boolean changePassword(String resetId, String newPassword);
}
