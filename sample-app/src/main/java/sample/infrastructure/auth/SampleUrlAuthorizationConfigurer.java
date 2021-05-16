package sample.infrastructure.auth;

import io.sitoolkit.util.sbrs.UrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
public class SampleUrlAuthorizationConfigurer implements UrlAuthorizationConfigurer {

  @Override
  public ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
      configure(
          ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
              registory) {

    return registory
        .antMatchers("/auth/**", "/account/**")
        .permitAll()
        .antMatchers("/admin/**")
        .hasRole("ADMIN")
        .anyRequest()
        .authenticated();
  }
}
