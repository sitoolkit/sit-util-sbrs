package io.sitoolkit.util.sbrs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(prefix = "sit.sbrs", name = "notify-type", havingValue = "mail")
@Component
public class EmailActivateCodeNotifier implements AcitvateCodeNotifier {

  @Autowired EmailApi emailApi;

  @Autowired SbrsNotifyActivateProperties notifyActivateProperties;

  @Override
  public void notify(
      String loginId, String to, String activateCode, Map<String, String> notifyParams) {
    EmailObject mailObj = new EmailObject();
    mailObj.setTo(Arrays.asList(to));
    mailObj.setSubject(notifyActivateProperties.getSub());
    mailObj.setFrom(notifyActivateProperties.getFrom());
    setMessage(mailObj, loginId, activateCode, notifyParams);
    emailApi.send(mailObj);
  }

  private void setMessage(
      EmailObject mailObj, String loginId, String activateCode, Map<String, String> notifyParams) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("loginId", loginId);
    variables.put("activateCode", activateCode);
    if (Objects.nonNull(notifyParams)) variables.putAll(notifyParams);
    mailObj.setTextMessage(
        EmailTemplateEngine.generate(notifyActivateProperties.getTextTemplate(), variables));
    if (StringUtils.isNotEmpty(notifyActivateProperties.getHtmlTemplate())) {
      mailObj.setHtmlMessage(
          EmailTemplateEngine.generate(notifyActivateProperties.getHtmlTemplate(), variables));
    }
  }
}
