package dev.mottolab.storeapi.config;

import dev.mottolab.storeapi.filter.JwtAuthenFilter;
import dev.mottolab.storeapi.repository.IdentifyRepository;
import dev.mottolab.storeapi.repository.UserInfoRepository;
import dev.mottolab.storeapi.service.UserInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthenFilter authenFilter;

    public SecurityConfiguration(@Lazy JwtAuthenFilter authenFilter){
        this.authenFilter = authenFilter;
    }

    @Bean
    public UserDetailsService userDetailsService(
            IdentifyRepository identifyRepository,
            UserInfoRepository userInfoRepository,
            PasswordEncoder passwordEncoder
    ) {
        return new UserInfoService(identifyRepository, userInfoRepository, passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        return http.authorizeHttpRequests(
                auth -> auth.requestMatchers("/authen/**", "/products/**", "/category/**").permitAll()
                        .requestMatchers("/payment/callback/scb/completed", "/payment/callback/chillpay/completed").permitAll()
                        .requestMatchers(HttpMethod.POST, "/products/create", "/category/create").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/products/**", "/category/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/products/**", "/category/**").authenticated()
                        .requestMatchers("/user/**", "/baskets/**", "/orders/**", "/payment/**").authenticated()
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(this.authenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf((httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return  config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }
}
