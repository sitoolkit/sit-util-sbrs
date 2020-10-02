package sample;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sitoolkit.util.sbrs.SbrsProperties;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.subethamail.wiser.Wiser;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleTest {

  @Autowired TestRestTemplate restTemplate;

  @Autowired SbrsProperties securityProperties;

  static ObjectMapper mapper;

  static Wiser wiser;

  @BeforeClass
  public static void setupBeforeClass() {
    mapper = new ObjectMapper();
    mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);

    wiser = new Wiser();
    wiser.setPort(1025);
    wiser.setHostname("localhost");
    wiser.start();
  }

  @After
  public void tearDown() {
    if (Objects.nonNull(wiser.getMessages()) && !wiser.getMessages().isEmpty()) {
      wiser.getMessages().clear();
    }
  }

  @AfterClass
  public static void tearDownAfterClass() {
    wiser.stop();
  }

  @Test
  public void testAccessWithoutToken() throws Exception {
    ResponseEntity<String> annonymousResponse = restTemplate.getForEntity("/user/ok", String.class);

    assertThat(annonymousResponse.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void testAuthentication() throws Exception {
    assertLogin("admin@sample.com", "password", "Administrator", "USERS,ADMINS");
  }

  @Test
  public void testAuthorization() throws Exception {
    String token = doLogin("user@sample.com", "password").getBody().get("token").toString();

    ResponseEntity<String> adminResponse = doGet("/admin", token, String.class);

    assertThat(adminResponse.getStatusCode(), is(HttpStatus.FORBIDDEN));

    ResponseEntity<String> userResponse = doGet("/user", token, String.class);

    assertThat(userResponse.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testLoginFailure() throws Exception {
    assertLoginFailure("admin", "incorrectpassword");
  }

  @SuppressWarnings({"rawtypes"})
  @Test
  public void testCreateUser() throws Exception {
    String userid = "newuser";
    String loginId = "newuser@sample.com";
    String notifyTo = "newuser@sample.com";
    String password = "password";
    String name = "NewUser";
    String roles = "USERS";

    ResponseEntity<Map> createResponse = doCreate(loginId, notifyTo);

    if (StringUtils.equals(securityProperties.getRegistoryType(), "ldap")) {
      assertThat(createResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));

    } else {
      assertThat(createResponse.getStatusCode(), is(HttpStatus.OK));
      assertThat(createResponse.getBody().get("success"), is(true));

      assertLoginFailure(userid, password);

      String mailMessage = getTextpartFromMultipartMail();
      String activateCode =
          StringUtils.substringBefore(
              StringUtils.substringAfter(mailMessage, "Activate code is "), ".");
      ResponseEntity<Map> activateResponse =
          doActivate(loginId, activateCode, password, name, roles);
      assertThat(activateResponse.getStatusCode(), is(HttpStatus.OK));
      assertThat(activateResponse.getBody().get("success"), is(true));

      assertLogin(loginId, password, name, roles);
    }
  }

  @SuppressWarnings({"rawtypes"})
  @Test
  public void testChangePassword() throws Exception {
    String userid = "changePw";
    String loginId = "changePw@sample.com";
    String mailAddress = "changePw@sample.com";
    String oldPassword = "password";
    String newPassword = "newPassword";
    String name = "ChangePassword";
    String roles = "USERS";

    assertLogin(loginId, oldPassword, name, roles);
    ResponseEntity<Map> resetPasswrodResponse = doResetPassword(mailAddress);

    if (StringUtils.equals(securityProperties.getRegistoryType(), "ldap")) {
      assertThat(resetPasswrodResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));

    } else {
      assertThat(resetPasswrodResponse.getStatusCode(), is(HttpStatus.OK));
      assertThat(resetPasswrodResponse.getBody().get("success"), is(true));

      String mailMessage = getTextpartFromMultipartMail();
      String resetId = StringUtils.trim(StringUtils.substringAfterLast(mailMessage, "/"));

      ResponseEntity<Map> changePasswordResponse = doChangePassword(resetId, newPassword);
      assertThat(changePasswordResponse.getStatusCode(), is(HttpStatus.OK));
      assertThat(changePasswordResponse.getBody().get("success"), is(true));

      assertLoginFailure(userid, oldPassword);
      assertLogin(loginId, newPassword, name, roles);
    }
  }

  private String getTextpartFromMultipartMail() throws Exception {
    MimeMultipart multipart =
        (MimeMultipart) wiser.getMessages().get(0).getMimeMessage().getContent();

    String textpart = "";
    for (int cnt = 0; cnt < multipart.getCount(); cnt++) {
      BodyPart bodyPart = multipart.getBodyPart(cnt);
      if (bodyPart.isMimeType("text/plain")) {
        textpart = bodyPart.getContent().toString();
        break;
      }
    }
    return textpart;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  void assertLogin(String loginId, String password, String name, String roles) throws Exception {
    ResponseEntity<Map> loginResponse = doLogin(loginId, password);
    assertThat(loginResponse.getStatusCode(), is(HttpStatus.OK));

    String token = loginResponse.getBody().get("token").toString();
    Map me = doGet("/auth/me", token, Map.class).getBody();

    assertThat(me.get("loginId"), is(loginId));
    assertThat((List<String>) me.get("roles"), containsInAnyOrder(roles.split(",")));

    Map ext = (Map) me.get("ext");
    assertThat(ext.get("name"), is(name));
  }

  @SuppressWarnings("rawtypes")
  void assertLoginFailure(String userId, String password) throws IOException {
    ResponseEntity<Map> loginResponse = doLogin(userId, password);

    assertThat(loginResponse.getStatusCode(), is(HttpStatus.OK));
    assertThat(loginResponse.getBody().get("success"), is(false));
  }

  @SuppressWarnings("rawtypes")
  ResponseEntity<Map> doLogin(String loginId, String password) throws IOException {
    return restTemplate.postForEntity(
        "/auth/login",
        json("{'loginId':'" + loginId + "', 'password': '" + password + "'}"),
        Map.class);
  }

  @SuppressWarnings("rawtypes")
  ResponseEntity<Map> doCreate(String loginId, String notifyTo) throws IOException {
    return restTemplate.postForEntity(
        "/account/create",
        json("{'loginId': '" + loginId + "', 'notifyTo': '" + notifyTo + "' }"),
        Map.class);
  }

  @SuppressWarnings("rawtypes")
  ResponseEntity<Map> doActivate(
      String loginId, String activateCode, String password, String name, String roles)
      throws IOException {
    return restTemplate.postForEntity(
        "/account/activate",
        json(
            "{'loginId': '"
                + loginId
                + "', 'activateCode': '"
                + activateCode
                + "', 'password': '"
                + password
                + "', 'ext': {"
                + "'name': '"
                + name
                + "', 'roles': '"
                + roles
                + "' }}"),
        Map.class);
  }

  @SuppressWarnings("rawtypes")
  ResponseEntity<Map> doResetPassword(String mailAddress) throws IOException {
    return restTemplate.postForEntity(
        "/account/resetPassword", json("{'notifyTo': '" + mailAddress + "'}"), Map.class);
  }

  @SuppressWarnings("rawtypes")
  ResponseEntity<Map> doChangePassword(String resetId, String newPassword) throws IOException {
    String changePasswordUrl = "/account/changePassword/" + resetId;
    return restTemplate.postForEntity(
        changePasswordUrl, json("{'newPassword': '" + newPassword + "'}"), Map.class);
  }

  @SuppressWarnings("unchecked")
  Map<String, String> json(String json) throws IOException {
    return mapper.readValue(json, Map.class);
  }

  <T> ResponseEntity<T> doGet(String url, String token, Class<T> type) throws URISyntaxException {
    RequestEntity<?> request =
        RequestEntity.get(new URI(url)).header("Authorization", "Bearer " + token).build();

    return restTemplate.exchange(request, type);
  }
}
