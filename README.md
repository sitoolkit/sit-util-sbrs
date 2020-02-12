# SI-Toolkit Utility for Spring Boot Rest Security

SI-Toolkit Utility for Spring Boot Rest Security (SBRS) is a Java libary for REST applictions using Spring Boot and Spring Security.

## How Does It Work ?

To explain the SBRS behavior, we provide a sample REST application.
You can run it with the following commands.

```sh
# Download the sample REST application 
curl https://repo.maven.apache.org/maven2/io/sitoolkit/util/sbrs/sample-app/0.9-SNAPSHOT/sample-app-0.9-SNAPSHOT.jar

# Run
java -jar sample-app.jar

# Login with HTTP POST 
curl -X POST -H "Content-Type: application/json" -d '{"loginId":"admin", "password":"password"}' localhost:8080/auth/login
```

The response body of POST includes a security token.

```json
{"loginId":"admin","token":"xxxxxx"}
```

Copy the token value and request GET with it.

```sh
curl -H 'Authorization:Bearer xxxxxx' localhost:8080/auth/me
```

Then you can get a response includes login user data.

```json
{"roles":["ADMIN", "USER"], "ext":{"name":"Administrator"}}
```

This sample application is build from [this project](sample-app).


## How To Use In Your Project

SBRS is a Java library published in Maven repository, you can use it in Maven / Gradle project. SBRS supports thease 2 types of user registory.

* DB
* LDAP

### Usage for DB User Registory 


#### Step 1 : Add Dependneces

Add dependency of sit-util-sbrs-core.

```xml
  <dependency>
    <groupId>io.sitoolkit.util.sbrs</groupId>
    <artifactId>sit-util-sbrs-core</artifactId>
    <version>0.9-SNAPSHOT</version>
  </dependency>
```


#### Step 2: Add property
Add property specifying user registory type to application.properties

```properties
sit.sbrs.registory-type=db
```

#### Step 3: Implement Java Classes

Create service class witch implements org.springframework.security.core.userdetails.UserDetailsService.
The sample application uses SpringDataJPA but you can use any db access libraries. 

* UserService

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserService implements UserDetailsService {

  @Autowired UserRepository repository;

  @Autowired PasswordEncoder encoder;

  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

    UserEntity entity =
        repository.findById(loginId).orElseThrow(() -> new UsernameNotFoundException("Login Failed"));

    SampleUser user =
        new SampleUser(
            entity.getId(),
            entity.getPassword(),
            entity.getName(),
            SpringSecurityUtils.toAuthrities(entity.getRoles().split(",")));

    return user;
  }
}

```

The sample app uses JPA as db access framework, Repository and Entity classes are as follows.

* UserRepository

```java
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {}
```

* UserEntity

```java
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserEntity {

  @Id private String id;
  private String password;
  private String name;
  private String roles;

  // getters and setters
}
```

Create service class witch implements org.springframework.security.core.userdetails.UserDetailsService.



```java
import io.sitoolkit.util.sbrs.TokenConverter;

@Component
public class TokenConverterDbImpl implements TokenConverter<SampleUser> {

  /**
   *  Convertion from UserDetailsService.loadUserByUsername result 
   *  to "ext" property of /auth/me response body. 
   **/
  @Override
  public Map<String, String> toTokenExt(SampleUser principal) {
    Map<String, String> ext = new HashMap<>();

    ext.put("name", principal.getName());

    return ext;
  }

  /**
   * Conversion from the token data in request header
   * to a object used in RestControllers and athrebackend java classes. 
   **/ 
  @Override
  public SampleUser toPrincipal(String loginId, List<String> roles, Map<String, String> ext) {
    return new SampleUser(loginId, roles, ext.get("name"));
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
