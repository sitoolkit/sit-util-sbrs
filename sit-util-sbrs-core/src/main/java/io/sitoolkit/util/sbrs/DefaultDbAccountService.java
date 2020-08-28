package io.sitoolkit.util.sbrs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultDbAccountService<T1 extends AccountEntity, T2 extends TmpAccountEntity>
    implements UserDetailsService, AccountService {

  @Autowired AccountRepository<T1> accountRepository;

  @Autowired TmpAccountRepository<T2> tmpAccountRepository;

  @Autowired Notifier notifier;

  @Autowired PasswordEncoder encoder;

  @Autowired ModelMapper modelMapper;

  @Override
  public LoginUser<T1> loadUserByUsername(String loginId) {
    T1 entity =
        accountRepository
            .findById(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("Login Failed"));

    return new LoginUser<>(
        entity.getId(), entity.getPassword(), entity, entity.getRoles().split(","));
  }

  @Override
  public boolean create(String loginId, String notifyTo, Map<String, String> ext) {
    if (accountRepository.findById(loginId).isPresent()) {
      return false;
    }

    String activateCode = ActivateCodeUtil.generate();
    tmpAccountRepository.save(createTmpAccount(loginId, activateCode, ext));
    notifier.activateCodeNotify(loginId, notifyTo, activateCode, ext);
    return true;
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public boolean activate(
      String loginId, String activateCode, String password, Map<String, String> ext) {

    T2 tmpAccount =
        tmpAccountRepository
            .findById(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("Account Not Found"));
    if (!ActivateCodeUtil.confirm(activateCode, tmpAccount.getActivateCode())) {
      return false;
    }

    Map<String, String> param = new HashMap<>();
    param.put("id", loginId);
    param.put("password", encoder.encode(password));
    if (Objects.nonNull(ext)) param.putAll(ext);
    T1 account =
        modelMapper.map(
            param,
            (Class<T1>)
                GenericClassUtil.getGenericClassFromImpl(
                    accountRepository.getClass(), AccountRepository.class, 0));

    accountRepository.save(account);
    tmpAccountRepository.deleteById(loginId);
    return true;
  }

  @SuppressWarnings({"unchecked"})
  private T2 createTmpAccount(String loginId, String activateCode, Map<String, String> ext) {
    T2 tmpAccount = tmpAccountRepository.findById(loginId).orElse(null);
    if (Objects.nonNull(tmpAccount)) {
      tmpAccount.setActivateCode(activateCode);
      modelMapper.map(ext, tmpAccount);
    } else {
      Map<String, String> param = new HashMap<>();
      param.put("id", loginId);
      param.put("activateCode", activateCode);
      if (Objects.nonNull(ext)) param.putAll(ext);
      tmpAccount =
          modelMapper.map(
              param,
              (Class<T2>)
                  GenericClassUtil.getGenericClassFromImpl(
                      tmpAccountRepository.getClass(), TmpAccountRepository.class, 0));
    }

    return tmpAccount;
  }
}
