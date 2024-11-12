package com.example.rasmusportfoliobackend.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    @Autowired
    CustomAuthenticationProvider customAuthProvider;

    @Autowired
    CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthProvider);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {

        http
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/loginsite").permitAll()
                                .requestMatchers("/addnewuser").permitAll()
                                .requestMatchers("/newUserMVC").permitAll()
                                .requestMatchers("/validateUserLogin").permitAll()
                                .requestMatchers("/admin_start_page", "/shipped_orders",
                                        "/orders_to_handle", "/mark_order",
                                        "/start_handling", "/allorders").hasAuthority("1")//lägg till samtliga adminsidor så här
                                .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/loginsite") // Specify the URL of the custom login page
                        .permitAll()
                        .loginProcessingUrl("/login")// Specify the custom login processing URL
                        .successHandler(new CustomAuthenticationSuccessHandler()) // Specify the success URL after successful login
                )
                .logout(logout -> logout
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(customAccessDeniedHandler) // Specify custom Access Denied handler
                )
                .authenticationManager(authManager);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
