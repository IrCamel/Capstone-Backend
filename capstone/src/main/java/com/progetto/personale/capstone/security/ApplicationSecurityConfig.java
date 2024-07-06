package com.progetto.personale.capstone.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Properties;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {

    @Bean
    PasswordEncoder stdPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    AuthTokenFilter authenticationJwtToken() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/users/login").permitAll()
                                .requestMatchers("/users").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/users/{id}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/users/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/users/{id}").authenticated()
                                .requestMatchers(HttpMethod.POST, "/users/{followerId}/follow/{followeeId}").authenticated()
                                .requestMatchers(HttpMethod.POST, "/users/{followerId}/unfollow/{followingId}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/users/{userId}/followers").authenticated()
                                .requestMatchers(HttpMethod.GET, "/users/{userId}/following").authenticated()

                                .requestMatchers(HttpMethod.POST, "/prodotti").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/prodotti/{id}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/prodotti/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/prodotti").authenticated()
                                .requestMatchers(HttpMethod.GET, "/prodotti/{id}").authenticated()

                                .requestMatchers(HttpMethod.POST, "/post").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/post/{postId}/user/{userId}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/post/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/post").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/post/{postId}/like/{userId}").authenticated()
                                .requestMatchers(HttpMethod.POST, "/post/{postId}/comment/{userId}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/post/{postId}/comments").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "post/comment/{commentId}/user/{userId}").authenticated()
                                .requestMatchers(HttpMethod.PUT,"post/{postId}/save/{userId}").authenticated()
                                .requestMatchers(HttpMethod.GET,"post/saved/{userId}").authenticated()

                                .requestMatchers(HttpMethod.POST, "/categorie").authenticated()
                                .requestMatchers(HttpMethod.GET, "/categorie").authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authenticationJwtToken(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JavaMailSenderImpl getJavaMailSender(@Value("${gmail.mail.transport.protocol}" ) String protocol,
                                                @Value("${gmail.mail.smtp.auth}" ) String auth,
                                                @Value("${gmail.mail.smtp.starttls.enable}" ) String starttls,
                                                @Value("${gmail.mail.debug}" ) String debug,
                                                @Value("${gmail.mail.from}" ) String from,
                                                @Value("${gmail.mail.from.password}" ) String password,
                                                @Value("${gmail.smtp.ssl.enable}" ) String ssl,
                                                @Value("${gmail.smtp.host}" ) String host,
                                                @Value("${gmail.smtp.port}" ) String port){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));

        mailSender.setUsername(from);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", debug);
        props.put("smtp.ssl.enable", ssl);

        return mailSender;
    }
}
