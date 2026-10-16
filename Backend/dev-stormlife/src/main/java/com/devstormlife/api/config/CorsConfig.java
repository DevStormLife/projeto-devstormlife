package com.devstormlife.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Permite qualquer origem (front-end)
        config.setAllowedOrigins(List.of("*"));
        
        // Permite qualquer cabeçalho
        config.setAllowedHeaders(List.of("*"));
        
        // Permite todos os métodos necessários para o CRUD
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // O "/**" aplica essa mesma regra de CORS para TODOS os endpoints do seu sistema de uma vez só
        source.registerCorsConfiguration("/**", config); 

        return source;
    }
}