package com.payment.paymentservice.config;

import com.payment.paymentservice.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private AuthenticationEntryPoint authenticationEntryPoint;
    private UserDetailsService userDetailsService;

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    public SecurityConfig(AuthenticationEntryPoint authenticationEntryPoint, UserDetailsService userDetailsService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Cambia esta URL según tu configuración
        //String jwkSetUri = "https://your-auth-server/.well-known/jwks.json";
        SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(userDetailsService, jwtSecret);
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeRequests((authorizeRequests) ->
                                authorizeRequests.requestMatchers("/payment/**").permitAll()
                               .anyRequest().authenticated()
            ).exceptionHandling(exception -> exception
                                .authenticationEntryPoint(authenticationEntryPoint)
             ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //oauth2ResourceServer(oauth2 -> oauth2.jwt())
        //http.oauth2ResourceServer(oauth2 -> oauth2.jwt());
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
