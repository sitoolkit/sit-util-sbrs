package io.sitoolkit.util.sbrs;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailApi {
  @Autowired SbrsSmtpProperties smtpProperties;

  private static Map<String, String> header = new HashMap<>();

  static {
    header.put("Content-Transfer-Encoding", "base64");
  }

  public void send(EmailObject mailObj) {
    try {
      Email email = initEmail(mailObj);
      email.send();
    } catch (EmailException e) {
      log.error("Send mail failure.", e);
      throw new IllegalArgumentException(e);
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
}
