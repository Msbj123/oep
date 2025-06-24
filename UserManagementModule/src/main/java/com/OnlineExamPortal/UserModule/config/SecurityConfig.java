package com.OnlineExamPortal.UserModule.config; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService; // This would typically be your CustomUserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.OnlineExamPortal.UserModule.Service.CustomUserDetailsService;

/**
 * Spring Security configuration class for the User Service.
 * Configures security filters, authentication providers, and authorization rules.
 */
@Configuration // Marks this class as a Spring configuration class
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables method-level security annotations (e.g., @PreAuthorize)
public class SecurityConfig {

    @Autowired // Injects the custom JWT authentication filter
    private JwtAuthFilter jwtAuthFilter;

    @Autowired // Injects the custom UserDetailsService (our CustomUserDetailsService)
    private UserDetailsService userDetailsService; // Assuming this is your CustomUserDetailsService now

    /**
     * Defines the SecurityFilterChain, which configures HTTP security.
     * @param http HttpSecurity object to configure security.
     * @return A configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) // Disable CSRF protection (common for stateless REST APIs)
                .authorizeHttpRequests(auth -> auth
                        // Allow unauthenticated access to registration, login, and service token endpoints
                        .requestMatchers("/examProtal/userModule/register", "/examProtal/userModule/login", "/examProtal/userModule/token/service","examProtal/userModule/{userId}/profile","api/admin/exams/{id}").permitAll()
                        // Allow ADMIN to view all users
                        .requestMatchers("/examProtal/userModule/users","/examProtal/userModule/{id}/role","qb/addQuestion","qb/addMultipleQuestions","qb/uploadFile","qb/getquestion/{id}","qb/getAll","qb/updQuestion/{id}","qb/delQuestion/{id}","qb/getByCategory/{category}","qb/getByDifficulty/{difficulty}","/api/admin/exams/update/{id}","/api/admin/exams/delete/{id}","/api/admin/exams/role/{id}").hasRole("ADMIN")
                        // Allow ADMIN and STUDENT to view their own profile and update
                        .requestMatchers("/examProtal/userModule/{id}").hasAnyRole("ADMIN", "STUDENT")
       
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        // Configure session management to be stateless (no sessions will be created or used)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider()) // Set our custom authentication provider
                // Add the JWT authentication filter before the standard UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build(); // Build the security filter chain
    }

    /**
     * Defines the PasswordEncoder bean. Uses BCryptPasswordEncoder for strong password hashing.
     * @return An instance of BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the AuthenticationProvider. Uses DaoAuthenticationProvider, which retrieves
     * user details from a UserDetailsService and authenticates with a PasswordEncoder.
     * @return An instance of DaoAuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService); // Set our custom UserDetailsService
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Set our password encoder
        return authenticationProvider;
    }

    /**
     * Defines the AuthenticationManager bean. It is responsible for authenticating
     * Authentication objects (e.g., from login attempts).
     * @param config The AuthenticationConfiguration from which to get the AuthenticationManager.
     * @return An instance of AuthenticationManager.
     * @throws Exception If an error occurs during manager retrieval.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
   
        

}