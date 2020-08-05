# SI-Toolkit Utility for Spring Boot Rest Security

SI-Toolkit Utility for Spring Boot Rest Security (SBRS) is a Java libary for REST applictions using Spring Boot and Spring Security.

## How Does It Work ?

To explain the SBRS behavior, we provide a sample REST application.
You can run it with the following commands.

```sh
# Download the sample REST application
curl https://repo.maven.apache.org/maven2/io/sitoolkit/util/sbrs/sample-app/1.0.0-SNAPSHOT/sample-app-1.0.0-SNAPSHOT.jar -o sample-app.jar

# Run
java -jar sample-app.jar

# Login with HTTP POST
curl -X POST -H "Content-Type: application/json" -d "{\"loginId\":\"admin\", \"password\":\"password\"}" localhost:8080/auth/login
```

The response body of POST includes a security token.

```json
{ "loginId": "admin", "token": "xxxxxx", "success": true }
```

Copy the token value and request GET with it.

```sh
curl -H "Authorization:Bearer xxxxxx" localhost:8080/auth/me
```

Then you can get a response includes login user data.

```json
{ "loginId": "admin", "roles": ["ADMINS", "USERS"], "ext": { "name": "Administrator" } }
```

This sample application is build from [this project](sample-app).

## How To Use In Your Project

SBRS is a Java library published in Maven repository, you can use it in Maven / Gradle project. SBRS supports thease 2 types of user registory.

- DB
- LDAP

### Usage for DB User Registory

#### Step 1 : Add Dependneces

Add dependency of sit-util-sbrs-core.

```xml
  <dependency>
    <groupId>io.sitoolkit.util.sbrs</groupId>
    <artifactId>sit-util-sbrs-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </dependency>
```

#### Step 2: Add Property

Add property specifying user registory type to application.properties

```properties
sit.sbrs.registory-type=db
```

#### Step3: Add Configuration

Import SbrsConfiguration to your spring boot application.

```java
import io.sitoolkit.util.sbrs.SbrsConfiguration;

@SpringBootApplication
@Import(SbrsConfiguration.class)
public class SampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(SampleApplication.class, args);
  }
}
```

#### Step 4: Implement Java Classes

The sample app uses JPA as db access framework, Repository and Entity classes are as follows.

- UserRepository  
  Create a Repository class which implements io.sitoolkit.util.sbrs.AccountRepository.

```java
import io.sitoolkit.util.sbrs.AccountRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AccountRepository<UserEntity> {}
```

- UserEntity  
  Create a Entity class which implements io.sitoolkit.util.sbrs.AccountEntity.

```java
import io.sitoolkit.util.sbrs.AccountEntity;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserEntity implements AccountEntity {

  // Member of AccountEntity's getter methods
  @Id private String id;
  private String password;
  private String roles;

  // Your columns
  private String name;
}
```

Create a class which implements io.sitoolkit.util.sbrs.TokenConverter.

```java
import io.sitoolkit.util.sbrs.LoginUser;
import io.sitoolkit.util.sbrs.TokenConverter;

@Component
public class TokenConverterDbImpl implements TokenConverter<LoginUser<UserEntity>> {

  /**
   *  Convertion from UserDetailsService.loadUserByUsername result
   *  to "ext" property of /auth/me response body.
   **/
  @Override
  public Map<String, String> toTokenExt(LoginUser<UserEntity> principal) {
    Map<String, String> ext = new HashMap<>();
    ext.put("name", principal.getEntity().getName());
    return ext;
  }

  /**
   * Conversion from the token data in request header
   * to a object used in RestControllers and other backend java classes.
   **/
  @Override
  public LoginUser<UserEntity> toPrincipal(
      String loginId, List<String> roles, Map<String, String> ext) {
    UserEntity entity = new UserEntity();
    entity.setName(ext.get("name"));
    return new LoginUser<>(loginId, entity, roles);
  }
}
```

You can use the result object of TokenConverter.toPrincipal in your RestController with @AuthenticationPrincipal.

```java
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
public class SampleController {

  @GetMapping("/user")
  public String user(@AuthenticationPrincipal UserDetails loginUser) {

    return "user";
  }

```

Create a class which implements io.sitoolkit.util.sbrs.UrlAuthorizationConfigurer to define url patterns which need authentication and authorization.

If you want to learn how to use "registory" (the argument of configure method),
see [the original spring security guide](https://docs.spring.io/spring-security/site/docs/current/guides/html5/helloworld-boot.html#creating-your-spring-security-configuration)

```java
import io.sitoolkit.util.sbrs.UrlAuthorizationConfigurer;

@Component
public class SampleUrlAuthorizationConfigurer implements UrlAuthorizationConfigurer {

  @Override
  public ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
      configure(
          ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
              registory) {

    return registory
        .antMatchers("/auth/**")
        .permitAll()
        .antMatchers("/admin/**")
        .hasRole("ADMIN")
        .anyRequest()
        .authenticated();
  }
}

```
