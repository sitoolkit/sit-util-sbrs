package io.sitoolkit.util.sbrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@ComponentScan
@EnableConfigurationProperties({
  SbrsProperties.class,
  SbrsSmtpProperties.class,
  SbrsNotificationProperties.class
})
@ConditionalOnWebApplication
public class SbrsConfiguration extends WebSecurityConfigurerAdapter {

  @SuppressWarnings("rawtypes")
  @Autowired
  @Qualifier("sitoolkitSequrityConfigurer")
  SecurityConfigurer securityConfigurer;

  @Autowired AuthencicationManagerConfigurer authencicationManagerConfigurer;

  @Autowired(required = false)
  UrlAuthorizationConfigurer urlAuthorizationConfigurer;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
        http.httpBasic()
            .disable()
            .csrf()
            .disable()
            .cors()
            .configurationSource(corsConfigurationSource())
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests();

    //        .antMatchers("/auth/**")
    //        .permitAll()
    //        .antMatchers("/admin/**")
    //        .hasRole("admin")
    //        .anyRequest()
    //        .authenticated()

    if (urlAuthorizationConfigurer == null) {
      registry.anyRequest().permitAll();
    } else {
      urlAuthorizationConfigurer.configure(registry);
    }

    registry.and().apply(securityConfigurer);
  }

  private CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
    corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
    corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
    corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

    return corsConfigurationSource;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    authencicationManagerConfigurer.configure(auth);
  }
}
