package io.sitoolkit.util.sbrs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivateCodeUtil {

  public static String generate() {
    return RandomStringUtils.randomNumeric(6);
  }

  public static boolean confirm(String input, String origin) {
    return StringUtils.equals(input, origin);
  }
}
