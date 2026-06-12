package com.devstormlife.api.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Permite que qualquer site/navegador faça requisições para a API
        config.addAllowedOrigin("*"); 
        
        // Permite o envio de qualquer cabeçalho HTTP (Content-Type, Autenticações, etc.)
        config.addAllowedHeader("*");
        
        // Permite todos os métodos HTTP necessários para o CRUD (GET, POST, PUT, DELETE)
        config.addAllowedMethod("*"); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // Liberação explícita de acesso endpoint por endpoint
        source.registerCorsConfiguration("/funcionarios/**", config);
        source.registerCorsConfiguration("/usuarios/**", config);
        source.registerCorsConfiguration("/usuarios/login/**", config);
        source.registerCorsConfiguration("/usuarios/cadastro/**", config);
        source.registerCorsConfiguration("/cartao/**", config);
        source.registerCorsConfiguration("/subestacoes/**", config);

        // Retorna a configuração estruturada para o ecossistema do Spring
        return new CorsFilter(source);
    }
}