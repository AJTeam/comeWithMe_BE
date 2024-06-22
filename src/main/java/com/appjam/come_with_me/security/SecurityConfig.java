package com.appjam.come_with_me.security;

import com.appjam.come_with_me.security.auth.PrincipalUserService;
import com.appjam.come_with_me.security.filter.TokenAuthenticationFilter;
import com.appjam.come_with_me.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Autowired
    private PrincipalUserService principalUserService;

    @Bean
    public BCryptPasswordEncoder PwdEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/user/**").authenticated();
                    req.requestMatchers("/mission/**").authenticated();
                    req.requestMatchers("/room/**").authenticated();
                    req.requestMatchers("/admin/**").hasAuthority("ADMIN");
                    req.anyRequest().permitAll();
                })
                .addFilterAfter(new TokenAuthenticationFilter(userService), LogoutFilter.class)
                .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }
}