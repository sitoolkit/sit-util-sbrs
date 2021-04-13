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
import org.springframework.http.MediaType;
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

  public T saveEmail(EmailObject mailObj) {
    return emailRepository.save(createEmailEntity(mailObj));
  }

  @Async
  public void send(String emailId) {
    T emailEntity = emailRepository.findById(emailId).orElse(null);
    if (null == emailEntity) {
      log.error("Cannot find email from database.  id: " + emailId);
      throw new IllegalArgumentException("The specified emailId does not exist in the database.");
    }

    try {
      Email email = initEmail(emailEntity);
      email.send();
      emailEntity.setSendStatus(EmailSendStatus.SENT.getValue());
    } catch (EmailException e) {
      emailEntity.setSendStatus(EmailSendStatus.FAILURE.getValue());
      log.error("Send mail failure. id: " + emailEntity.getId(), e);
      throw new IllegalArgumentException(e);
    } finally {
      emailRepository.save(emailEntity);
    }
  }

  private Email initEmail(T emailEntity) throws EmailException {
    Email mail;
    if (MediaType.TEXT_PLAIN_VALUE.equals(emailEntity.getMimeType())) {
      mail = new SimpleEmail();
      mail.setMsg(emailEntity.getTextMessage());
    } else {
      mail = new HtmlEmail();
      ((HtmlEmail) mail).setHtmlMsg(emailEntity.getHtmlMessage());
      ((HtmlEmail) mail).setTextMsg(emailEntity.getTextMessage());
    }

    setConnectionParams(mail);
    setSendParms(mail, emailEntity);
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

  private void setSendParms(Email email, T emailEntity) throws EmailException {
    email.setCharset("UTF-8");
    email.setHeaders(header);
    email.setFrom(emailEntity.getFrom());
    email.addTo(emailEntity.getTo().split(","));
    email.setSubject(emailEntity.getSubject());
  }

  private boolean needAuth() {
    return StringUtils.isNotEmpty(smtpProperties.getSmtpUser())
        && StringUtils.isNotEmpty(smtpProperties.getSmtpPassword());
  }

  private DefaultAuthenticator smtpAuth() {
    return new DefaultAuthenticator(smtpProperties.getSmtpUser(), smtpProperties.getSmtpPassword());
  }

  @SuppressWarnings({ "unchecked" })
  private T createEmailEntity(EmailObject mailObj) {
    Map<String, String> param = new HashMap<>();
    param.put("id", UUID.randomUUID().toString());
    param.put("from" , mailObj.getFrom());
    param.put("to", String.join(",", mailObj.getTo()));
    param.put("subject", mailObj.getSubject());
    param.put("textMessage", mailObj.getTextMessage());
    param.put("htmlMessage", mailObj.getHtmlMessage());
    param.put("mimeType" , mailObj.getMimeType());
    param.put("sendStatus", EmailSendStatus.UNSENT.getValue());

    return modelMapper.map(param,
        (Class<T>) GenericClassUtil.getGenericClassFromImpl(emailRepository.getClass(), EmailRepository.class, 0));
  }
}
