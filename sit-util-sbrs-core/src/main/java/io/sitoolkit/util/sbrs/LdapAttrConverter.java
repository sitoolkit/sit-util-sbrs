package io.sitoolkit.util.sbrs;

import java.util.Map;

public interface LdapAttrConverter {
  Map<String, String> convert(Map<String, String> args);
}
