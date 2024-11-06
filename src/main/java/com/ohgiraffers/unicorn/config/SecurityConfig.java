package com.ohgiraffers.unicorn.config;

import com.ohgiraffers.unicorn.error.exception.Exception401;
import com.ohgiraffers.unicorn.error.exception.Exception403;
import com.ohgiraffers.unicorn.jwt.JWTTokenFilter;
import com.ohgiraffers.unicorn.jwt.JWTTokenProvider;
import com.ohgiraffers.unicorn.utils.CustomCorpDetail;
import com.ohgiraffers.unicorn.utils.CustomIndivDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTTokenProvider jwtTokenProvider;
    private final CustomIndivDetail customIndivDetail;
    private final CustomCorpDetail customCorpDetail;

    private static final String[] WHITE_LIST = {
            "/api/v1/auth/**", "/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain corpFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/v1/auth/corp/**")
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JWTTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .formLogin(formLogin -> formLogin.disable())
                .userDetailsService(customCorpDetail)
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated());

        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/v1/auth/indiv/**")
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .formLogin(formLogin -> formLogin.disable())
                .userDetailsService(customIndivDetail)
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(authenticationEntryPoint());
                    exception.accessDeniedHandler(accessDeniedHandler());
                });

        return httpSecurity.build();
    }

    @Bean
    @Primary
    public AuthenticationManager customAuthenticationManager() {

        DaoAuthenticationProvider corpAuthProvider = new DaoAuthenticationProvider();
        corpAuthProvider.setUserDetailsService(customCorpDetail);
        corpAuthProvider.setPasswordEncoder(passwordEncoder());

        DaoAuthenticationProvider indivAuthProvider = new DaoAuthenticationProvider();
        indivAuthProvider.setUserDetailsService(customIndivDetail);
        indivAuthProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(Arrays.asList(corpAuthProvider, indivAuthProvider));
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            throw new Exception401("Authentication failed: " + authException.getMessage());
        };
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            throw new Exception403("Access denied: " + accessDeniedException.getMessage());
        };
    }
}
