/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.palletpals.config;

import ch.fhnw.palletpals.data.domain.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ch.fhnw.palletpals.business.service.UserDetailsServiceImpl;
import onl.mrtn.security.config.EnableTokenSecurity;
import onl.mrtn.security.service.TokenService;
import onl.mrtn.security.web.TokenAuthenticationFilter;
import onl.mrtn.security.web.TokenLoginFilter;
import onl.mrtn.security.web.TokenLogoutHandler;

@EnableWebSecurity
@EnableTokenSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String LOGIN_URL = "/api/user/login";
    private final String LOGOUT_URL = "/api/user/logout";

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
            .requiresChannel().requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null).requiresSecure().and() // If the X-Forwarded-Proto header is present, redirect to HTTPS (Heroku)
            .cors()
            .and()
            .csrf()
                .disable()
            .authorizeRequests()
                .antMatchers("/", "/assets/**", LOGIN_URL, LOGOUT_URL, "/api/user/register").permitAll()
                .antMatchers(HttpMethod.GET, "/api/products/**", "/api/product-images/**", "/api/validateAdmin", "/api/validate").permitAll()
                .antMatchers("/api/profile", "/api/orders/**", "/api/shopping/**").hasRole(UserType.USER.toString())
                .antMatchers("/api/profile", "/api/orders/**", "/api/shopping/**").hasRole(UserType.Admin.toString())
                .antMatchers("/api/products/**", "/api/product-images/**", "/api/serviceproviders/**", "/api/warehouses/**").hasRole(UserType.Admin.toString())
                .anyRequest().authenticated()
                .and()
                .addFilter(new TokenLoginFilter(authenticationManagerBean(), tokenService, LOGIN_URL))
                .addFilter(new TokenAuthenticationFilter(authenticationManagerBean(), tokenService))
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))    
                .addLogoutHandler(new TokenLogoutHandler(tokenService))
                // override success handler to prevent redirection
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                });
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}