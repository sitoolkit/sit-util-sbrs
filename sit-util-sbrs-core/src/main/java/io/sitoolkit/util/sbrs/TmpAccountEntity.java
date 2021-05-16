package io.sitoolkit.util.sbrs;

public interface TmpAccountEntity {
  String getId();

  String getLoginId();

  String getActivateCode();

  String getMailAddress();

  void setActivateCode(String activateCode);
}
