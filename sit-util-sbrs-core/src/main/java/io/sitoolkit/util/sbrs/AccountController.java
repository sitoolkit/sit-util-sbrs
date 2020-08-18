package io.sitoolkit.util.sbrs;

import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

  @Autowired AccountService accountService;

  @PostMapping("/create")
  public CreateResponseDto create(
      @RequestBody CreateRequestDto request, HttpServletResponse response) {

    boolean success = false;
    try {
      success =
          accountService.create(request.getLoginId(), request.getPassword(), request.getExt());

    } catch (UnsupportedOperationException e) {
      log.warn("Method not supported.", e);
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    return CreateResponseDto.builder().loginId(request.getLoginId()).success(success).build();
  }
}
