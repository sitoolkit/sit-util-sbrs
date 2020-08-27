package io.sitoolkit.util.sbrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnProperty(
    prefix = "sit.sbrs",
    name = "registory-type",
    havingValue = "db",
    matchIfMissing = true)
@RestController
@RequestMapping("/account")
public class AccountController {

  @Autowired AccountService accountService;

  @PostMapping("/create")
  public CreateResponseDto create(@RequestBody CreateRequestDto request) {

    return CreateResponseDto.builder()
        .loginId(request.getLoginId())
        .success(
            accountService.create(request.getLoginId(), request.getNotifyTo(), request.getExt()))
        .build();
  }

  @PostMapping("/activate")
  public ActivateResponseDto activate(@RequestBody ActivateRequestDto request) {
    return ActivateResponseDto.builder()
        .loginId(request.getLoginId())
        .success(
            accountService.activate(
                request.getLoginId(),
                request.getActivateCode(),
                request.getPassword(),
                request.getExt()))
        .build();
  }
}
