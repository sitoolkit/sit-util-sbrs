package io.sitoolkit.util.sbrs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailApi<T extends EmailEntity> {
  
  @Autowired SbrsSmtpProperties smtpProperties;
  @Autowired EmailRepository<T> emailRepository;
  @Autowired ModelMapper modelMapper;

  private static Map<String, String> header = new HashMap<>();

  static {
    header.put("Content-Transfer-Encoding", "base64");
  }

  @Async
  public void send(EmailObject mailObj) {

    T emailEntity = createEmailEntity(mailObj);
    emailRepository.save(emailEntity);
    emailEntity = emailRepository.findById(emailEntity.getId()).orElse(null);

    if (emailEntity == null) {
      log.error("Not found mail from database.");
      return;
    } 

    try {
      Email email = initEmail(mailObj);
      email.send();
      emailEntity.setSendStatus("1");
    } catch (EmailException e) {
      emailEntity.setSendStatus("2");
      log.error("Send mail failure. id: " + emailEntity.getId(), e);
      throw new IllegalArgumentException(e);
    } finally {
      emailRepository.save(emailEntity);
    }
  }

  private Email initEmail(EmailObject mailObj) throws EmailException {
    Email mail;
    if (StringUtils.isEmpty(mailObj.getHtmlMessage())) {
      mail = new SimpleEmail();
      mail.setMsg(mailObj.getTextMessage());
    } else {
      mail = new HtmlEmail();
      ((HtmlEmail) mail).setHtmlMsg(mailObj.getHtmlMessage());
      ((HtmlEmail) mail).setTextMsg(mailObj.getTextMessage());
    }

    setConnectionParams(mail);
    setSendParms(mail, mailObj);
    return mail;
  }

  private void setConnectionParams(Email email) {
    email.setHostName(smtpProperties.getSmtpHost());
    email.setSmtpPort(smtpProperties.getSmtpPort().intValue());
    email.setSSLCheckServerIdentity(true);
    email.setStartTLSEnabled(true);
    if (needAuth()) {
      email.setAuthenticator(smtpAuth());
    }
  }

  private void setSendParms(Email email, EmailObject mailObj) throws EmailException {
    email.setCharset("UTF-8");
    email.setHeaders(header);
    email.setFrom(mailObj.getFrom());
    email.addTo(mailObj.getTo().toArray(new String[mailObj.getTo().size()]));
    email.setSubject(mailObj.getSubject());
  }

  private boolean needAuth() {
    return StringUtils.isNotEmpty(smtpProperties.getSmtpUser())
        && StringUtils.isNotEmpty(smtpProperties.getSmtpPassword());
  }

  private DefaultAuthenticator smtpAuth() {
    return new DefaultAuthenticator(smtpProperties.getSmtpUser(), smtpProperties.getSmtpPassword());
  }

  @SuppressWarnings({ "unchecked" })
  private T createEmailEntity(EmailObject emailObject) {

    Map<String, String> param = new HashMap<>();
    param.put("id", UUID.randomUUID().toString());
    param.put("to", String.join(",", emailObject.getTo()));
    param.put("subject", emailObject.getSubject());
    param.put("message", emailObject.getTextMessage());
    param.put("sendStatus", "0");

    return modelMapper.map(param,
        (Class<T>) GenericClassUtil.getGenericClassFromImpl(emailRepository.getClass(), EmailRepository.class, 0));
  }
}
