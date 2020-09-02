package io.sitoolkit.util.sbrs;

public interface AccountEntity {
  String getId();

  String getPassword();

  void setPassword(String password);

  String getMailAddress();

  String getRoles();
}
