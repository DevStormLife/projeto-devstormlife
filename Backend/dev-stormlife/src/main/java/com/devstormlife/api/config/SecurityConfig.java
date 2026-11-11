package com.devstormlife.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            // 1. Ativa o CORS integrado para conversar com a sua classe CorsConfig
            .cors(Customizer.withDefaults()) 
            
            // 2. Desabilita o CSRF (essencial para requisições POST via API REST)
            .csrf(csrf -> csrf.disable())
            
            // 3. Libera as rotas públicas (Login, Cadastros, etc.)
            .authorizeHttpRequests(auth -> auth
                // Libera requisições de checagem CORS
            		.requestMatchers("/api/auth/**").permitAll()
                    
                    // 1. Permissões de Cadastro (Disponível para TODOS os cargos autenticados: BASICO, SUPERVISOR, CHEFE)
                    .requestMatchers(HttpMethod.OPTIONS, "/api/usuarios/**", "/api/funcionarios/**", "/api/cartoes/**").hasAnyRole("BASICO", "SUPERVISOR", "CHEFE")
                    
                    // 2. Relatórios de Subestação (Disponível para SUPERVISOR e CHEFE)
                    .requestMatchers("/api/subestacoes/relatorios/**").hasAnyRole("SUPERVISOR", "CHEFE")
                    
                    // 3. Acesso total / Páginas administrativas (Disponível apenas para CHEFE)
                    .requestMatchers("/api/subestacoes/**").hasRole("CHEFE")
                    .requestMatchers("/api/admin/**").hasRole("CHEFE")
                    
                    // Qualquer outra requisição precisa estar autenticada
                    .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {}); // ou configuração de JWT / FormLogin
 
        return http.build();
    }
}