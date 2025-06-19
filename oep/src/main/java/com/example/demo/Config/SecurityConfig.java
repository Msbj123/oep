package com.example.demo.Config;

import com.example.demo.Service.CustomUserDetailsService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    // PasswordEncoder bean for hashing passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // AuthenticationProvider to use CustomUserDetailsService and PasswordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // AuthenticationManager bean to handle authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // SecurityFilterChain to configure HTTP security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API
            .authorizeHttpRequests(authorize -> authorize
                // Allow unauthenticated access to registration and login
                .requestMatchers("/examProtal/userModule/register", "/examProtal/userModule/login","/api/admin/exams/getAllExams","/api/admin/exams/getExam/{id}","/examProtal/userModule/getById/{id}","/analytics/reports/user/{userId}","/analytics/reports/user/{userId}/exam/{examId}").permitAll()
                // ADMIN can access all users and update roles
                .requestMatchers("/examProtal/userModule/users", "/examProtal/userModule/{id}/role","qb/addQuestion","qb/addMultipleQuestions","qb/uploadFile","qb/getquestion/{id}","qb/getAll","qb/updQuestion/{id}","qb/delQuestion/{id}","qb/getByCategory/{category}","qb/getByDifficulty/{difficulty}","/api/admin/exams/update/{id}","/api/admin/exams/delete/{id}","/api/admin/exams/role/{id}","/mapping/{examId}/{questionId}","/mapping/exam/{examId}","/mapping/allexams","mapping/exam/{examId}/adminQues","/api/exams/exam/{examId}","/api/exams/{examId}/responses/{userId}","/analytics/reports/","/analytics/reports","/analytics/reports/topper","/analytics/reports/all").hasRole("ADMIN")
                // Other endpoints require authentication
                .requestMatchers("qb/attemptQuestions","mapping/exam/{examId}/questions","/api/exams/{examId}/submit","/api/exams/responses/{userId}","/analytics/reports/exams/{examId}","/analytics/reports/rank").hasAnyRole("ADMIN","STUDENT")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless sessions for JWT
            )
            .authenticationProvider(authenticationProvider()) // Set custom authentication provider
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before default authentication filter

        return http.build();
    }
}