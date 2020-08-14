package io.sitoolkit.util.sbrs;

public interface LdapInfo {
  String getAccountBaseDn();

  String getAccountIdentifierElement();

  String getGroupBaseDn();

  String getGroupIdentifierElement();

  String[] getObjectClasses();
}
