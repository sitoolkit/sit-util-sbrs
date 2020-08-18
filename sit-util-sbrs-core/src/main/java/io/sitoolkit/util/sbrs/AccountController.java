package io.sitoolkit.util.sbrs;

import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

  @Autowired(required = false)
  AccountService accountService;

  @Autowired SbrsProperties sbrsProperties;

  private static final String TYPE_LDAP = "ldap";

  @PostMapping("/create")
  public CreateResponseDto create(
      @RequestBody CreateRequestDto request, HttpServletResponse response) {

    if (StringUtils.equals(sbrsProperties.getRegistoryType(), TYPE_LDAP)) {
      log.warn("craete is not supported in LDAP mode.");
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return response(request.getLoginId(), false);
    }

    return response(
        request.getLoginId(),
        accountService.create(request.getLoginId(), request.getPassword(), request.getExt()));
  }

  private CreateResponseDto response(String loginId, boolean success) {
    return CreateResponseDto.builder().loginId(loginId).success(success).build();
  }
}
