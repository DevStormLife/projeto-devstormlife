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
        
        config.setAllowedOrigins(List.of("*")); 
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Libera para a API inteira rodar sem travar em sub-rotas
        source.registerCorsConfiguration("/**", config); 
        // Permite que qualquer site/navegador faça requisições para a API
        config.addAllowedOrigin("*"); 
        
        // Permite o envio de qualquer cabeçalho HTTP (Content-Type, Autenticações, etc.)
        config.addAllowedHeader("*");
        
        // Permite todos os métodos HTTP necessários para o CRUD (GET, POST, PUT, DELETE)
        config.addAllowedMethod("*"); 
        
        UrlBasedCorsConfigurationSource source1 = new UrlBasedCorsConfigurationSource();
        
        // Liberação explícita de acesso endpoint por endpoint

        source1.registerCorsConfiguration("/funcionarios", config);
        source1.registerCorsConfiguration("/usuarios", config);
        source1.registerCorsConfiguration("/usuarios/login", config);
        source1.registerCorsConfiguration("/usuarios/cadastro", config);
        source1.registerCorsConfiguration("/cartao", config);
        source1.registerCorsConfiguration("/subestacoes", config);
        source1.registerCorsConfiguration("/funcionarios/**", config);
        source1.registerCorsConfiguration("/usuarios/**", config);
        source1.registerCorsConfiguration("/usuarios/login/**", config);
        source1.registerCorsConfiguration("/usuarios/cadastro/**", config);
        source1.registerCorsConfiguration("/cartao/**", config);
        source1.registerCorsConfiguration("/subestacoes/**", config);

        return source1;
    }
}