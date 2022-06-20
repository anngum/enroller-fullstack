package com.company.enroller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Value("string value")
    private String secret;

    @Value("string value")
    private String issuer;

    @Value("int value")
    private int tokenExpiration;

   @Override
   protected void configure(HttpSecurity http) throws Exception {

       http.csrf().disable()
               .addFilterBefore(new JWTAuthenticationFilter(authenticationManager(), secret, issuer, tokenExpiration), UsernamePasswordAuthenticationFilter.class)
               .authorizeRequests()
               .anyRequest().permitAll()
               .and()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

   }




}