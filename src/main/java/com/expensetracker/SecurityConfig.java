package com.SpringBootMVC.ExpensesTracker.security;

import com.SpringBootMVC.ExpensesTracker.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationSuccessHandler customAuthenticationSuccessHandler,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService) throws Exception {

        http
            .authorizeHttpRequests(config ->
                config
                    .requestMatchers("/css/**").permitAll()
                    .requestMatchers("/assets/**").permitAll()
                    .requestMatchers("/js/**").permitAll()
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/showRegistrationForm").permitAll()
                    .requestMatchers("/processRegistration").permitAll()
                    .requestMatchers("/showLoginPage").permitAll()
                    .requestMatchers("/oauth2/**").permitAll()
                    .requestMatchers("/login/oauth2/**").permitAll()
                    .anyRequest().authenticated()
            )

            // Normal username/password login
            .formLogin(form ->
                form
                    .loginPage("/showLoginPage")
                    .loginProcessingUrl("/authenticateTheUser")
                    .successHandler(customAuthenticationSuccessHandler)
                    .permitAll()
            )

            // Google Login
            .oauth2Login(oauth ->
                oauth
                    .loginPage("/showLoginPage")
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                    .successHandler(customAuthenticationSuccessHandler)
            )

            .logout(logout ->
                logout
                    .permitAll()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/showLoginPage")
            );

        return http.build();
    }
    }