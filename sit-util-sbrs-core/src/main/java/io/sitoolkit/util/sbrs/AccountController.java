package io.sitoolkit.util.sbrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

  @Autowired DefaultAccountService defaultAccountService;

  @PostMapping("/create")
  public CreateResponseDto create(@RequestBody CreateRequestDto request) {
    boolean success =
        defaultAccountService.create(request.getLoginId(), request.getPassword(), request.getExt());

    return CreateResponseDto.builder().loginId(request.getLoginId()).success(success).build();
  }
}
