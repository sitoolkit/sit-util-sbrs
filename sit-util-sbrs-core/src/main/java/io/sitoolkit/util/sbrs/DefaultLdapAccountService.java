package io.sitoolkit.util.sbrs;

import java.util.Map;

public class DefaultLdapAccountService implements AccountService {

  private static final String NOT_SUPPORTED = " is not supported in LDAP mode.";

  @Override
  public boolean create(String loginId, String password, Map<String, String> ext) {
    throw new UnsupportedOperationException(getMethodName() + NOT_SUPPORTED);
  }

  private String getMethodName() {
    return Thread.currentThread().getStackTrace()[2].getMethodName();
  }
}
