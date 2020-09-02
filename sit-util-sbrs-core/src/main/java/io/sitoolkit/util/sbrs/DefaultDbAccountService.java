package io.sitoolkit.util.sbrs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultDbAccountService<
        T1 extends AccountEntity, T2 extends TmpAccountEntity, T3 extends ResetPasswordEntity>
    implements UserDetailsService, AccountService {

  @Autowired AccountRepository<T1> accountRepository;

  @Autowired TmpAccountRepository<T2> tmpAccountRepository;

  @Autowired ResetPasswordRepository<T3> resetPasswordRepository;

  @Autowired Notifier notifier;

  @Autowired PasswordEncoder encoder;

  @Autowired ModelMapper modelMapper;

  @Autowired SbrsProperties sbrsProperties;

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
    if (StringUtils.equals(sbrsProperties.getNotifyType(), "mail")) {
      if (Objects.isNull(ext)) {
        ext = new HashMap<>();
      }
      ext.put("mailAddress", notifyTo);
    }
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
    if (StringUtils.equals(sbrsProperties.getNotifyType(), "mail"))
      param.put("mailAddress", tmpAccount.getMailAddress());
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

  @SuppressWarnings({"unchecked"})
  @Override
  public boolean resetPassword(String notifyTo, Map<String, String> ext) {
    T1 account = accountRepository.findByMailAddress(notifyTo).orElse(null);
    if (Objects.isNull(account)) {
      return false;
    }

    String uuid = UUID.randomUUID().toString();
    Map<String, String> param = new HashMap<>();
    param.put("id", uuid);

    T3 resetPassword = resetPasswordRepository.findByAccountId(account.getId()).orElse(null);
    if (Objects.isNull(resetPassword)) {
      param.put("accountId", account.getId());
      resetPassword =
          modelMapper.map(
              param,
              (Class<T3>)
                  GenericClassUtil.getGenericClassFromImpl(
                      resetPasswordRepository.getClass(), ResetPasswordRepository.class, 0));
    } else {
      modelMapper.map(param, resetPassword);
    }

    resetPasswordRepository.save(resetPassword);

    String resetUrl = sbrsProperties.getChangePasswordUrl() + "/" + uuid;
    notifier.resetPasswordNotify(account.getId(), notifyTo, resetUrl, ext);
    return true;
  }

  @Override
  public boolean changePassword(String resetId, String newPassword) {
    T3 changePassword = resetPasswordRepository.findById(resetId).orElse(null);
    if (Objects.isNull(changePassword)) {
      return false;
    }

    T1 account =
        accountRepository
            .findById(changePassword.getAccountId())
            .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

    account.setPassword(encoder.encode(newPassword));
    accountRepository.save(account);
    resetPasswordRepository.deleteById(resetId);
    return true;
  }
}
