package io.sitoolkit.util.sbrs.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component("sitoolkitSequrityConfigurer")
public class JwtConfigurer
    extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  @Autowired JwtTokenFilter customFilter;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
