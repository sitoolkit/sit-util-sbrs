package io.sitoolkit.util.sbrs;

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
  public CreateResponseDto create(@RequestBody CreateRequestDto request) {
    try {
      boolean success =
          accountService.create(request.getLoginId(), request.getPassword(), request.getExt());
      return CreateResponseDto.builder().loginId(request.getLoginId()).success(success).build();

    } catch (MethodNotSupportedExcpetion e) {
      log.warn("Method not supported.", e);
      throw e;
    }
  }
}
