package io.suraj.projects.lovable.config;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebConfiguration {

    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(keycloakJwtAuthenticationConverter);
        return httpSecurity
                .cors(cors->cors.configurationSource(corsConfig())) //overrided in this class already
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/signup","/api/auth/login","/api/auth/users").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2->
                        oauth2.jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfig(){
        CorsConfiguration cors= new CorsConfiguration();
        cors.setAllowedOrigins(List.of("http://localhost:4200","http://localhost:4200/"));
        cors.setAllowCredentials(true);
        cors.setAllowedMethods(List.of("*"));
        cors.setAllowedHeaders(List.of("*"));
        cors.setMaxAge(600000L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        /*
            will later config the patterns and other cors methods
         */
        source.registerCorsConfiguration("/**",cors);
        return source;
    }

}
