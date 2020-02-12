package io.sitoolkit.util.sbrs;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SpringSecurityUtils {

  public static List<String> toStringList(Collection<? extends GrantedAuthority> collection) {
    return collection
        .stream()
        .map(GrantedAuthority::getAuthority)
        .map(authority -> StringUtils.substringAfter(authority, "ROLE_"))
        .collect(Collectors.toList());
  }

  public static List<GrantedAuthority> toAuthrities(Collection<String> roles) {
    return roles
        .stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toList());
  }

  public static List<GrantedAuthority> toAuthrities(String... roles) {
    return toAuthrities(Arrays.asList(roles));
  }
}
