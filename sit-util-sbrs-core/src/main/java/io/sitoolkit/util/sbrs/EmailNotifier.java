package io.sitoolkit.util.sbrs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(prefix = "sit.sbrs", name = "notify-type", havingValue = "mail")
@Component
public class EmailNotifier implements Notifier {

  @Autowired EmailApi emailApi;

  @Autowired SbrsNotificationProperties notificationProperties;

  @Override
  public void activateCodeNotify(
      String loginId, String to, String activateCode, Map<String, String> notifyParams) {
    EmailObject emailObj =
        initEmail(Arrays.asList(to), notificationProperties.getActivateSubject());

    Map<String, Object> variables = new HashMap<>();
    variables.put("loginId", loginId);
    variables.put("activateCode", activateCode);
    if (Objects.nonNull(notifyParams)) variables.putAll(notifyParams);

    setMessage(emailObj, "activate", variables);
    emailApi.send(emailObj);
  }

  @Override
  public void resetPasswordNotify(
      String loginId, String to, String changeUrl, Map<String, String> notifyParams) {
    EmailObject emailObj =
        initEmail(Arrays.asList(to), notificationProperties.getResetPasswordSubject());

    Map<String, Object> variables = new HashMap<>();
    variables.put("loginId", loginId);
    variables.put("changeUrl", changeUrl);
    if (Objects.nonNull(notifyParams)) variables.putAll(notifyParams);

    setMessage(emailObj, "resetPassword", variables);
    emailApi.send(emailObj);
  }

  private EmailObject initEmail(List<String> to, String subject) {
    EmailObject emailObj = new EmailObject();
    emailObj.setFrom(notificationProperties.getFrom());
    emailObj.setTo(to);
    emailObj.setSubject(subject);
    return emailObj;
  }

  private void setMessage(EmailObject emailObj, String template, Map<String, Object> variables) {

    String textTemplate = templateDir() + "/" + template + ".txt";
    emailObj.setTextMessage(EmailTemplateEngine.generate(textTemplate, variables));

    String htmlTemplate = templateDir() + "/" + template + ".html";
    if (Objects.nonNull(ClassLoader.getSystemResource(htmlTemplate))) {
      emailObj.setHtmlMessage(EmailTemplateEngine.generate(htmlTemplate, variables));
    }
  }

  private String templateDir() {
    return StringUtils.isEmpty(notificationProperties.getTemplateDir())
        ? "template"
        : notificationProperties.getTemplateDir();
  }
}
