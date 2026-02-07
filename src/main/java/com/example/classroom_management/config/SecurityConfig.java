package com.example.classroom_management.config;

import com.example.classroom_management.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inject our new filter
    private final JwtAuthFilter jwtAuthFilter;
    // Inject CustomUserDetailsService
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Login
                        .requestMatchers("/auth/login","/auth/refresh",  "/auth/logout").permitAll()

                        // Action
                        .requestMatchers(HttpMethod.POST, "/students").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("password123")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails student = User.withDefaultPasswordEncoder()
//                .username("student")
//                .password("learn")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, student);
//    }

// Specific rules for /students
//                        .requestMatchers(HttpMethod.POST, "/students/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/students/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/students/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/students/**").hasAnyRole("USER", "ADMIN")
//
//                        // General rules for other endpoints
//                        .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("USER", "ADMIN")