package io.sitoolkit.util.sbrs;

public interface AuthenticationService {
  String authenticate(String loginId, String password);
}
