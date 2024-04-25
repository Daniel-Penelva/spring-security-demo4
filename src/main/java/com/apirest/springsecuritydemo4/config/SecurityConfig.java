package com.apirest.springsecuritydemo4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.authorizeHttpRequests(auth -> auth                                                        // define as regras de autorização para as solicitações HTTP.
                        .requestMatchers("/", "/user/save", "/product/all").permitAll()    // permite acesso a todas as solicitações que correspondem aos caminhos "/", "/user/save" e "/product/all".
                        .anyRequest().authenticated())                                                 // exige autenticação para todas as outras solicitações.
                .httpBasic(withDefaults())                                                             // define a autenticação básica como o método de autenticação padrão.
                .formLogin(withDefaults())                                                             // define o login via formulário como o método de autenticação padrão.                     
                .csrf(AbstractHttpConfigurer::disable);                                                // desabilita a proteção contra ataques CSRF (Cross-Site Request Forgery).
        
        return http.build();                                                                           // retorna o objeto SecurityFilterChain configurado.

    }

    
    /* É um serviço de detalhes do usuário que é responsável por carregar as informações do usuário 
    a partir de um repositório de dados, como um banco de dados.*/ 
    @Bean
    public UserDetailsService userDetailsService() {
        return new OurUserInfoUserDetailsService();
    }


    /*Método de fábrica que cria e configura um provedor de autenticação do tipo DaoAuthenticationProvider. 
    Ele é retornado como um objeto de bean. Esse bean será automaticamente injetado em outros componentes do Spring Security 
    que precisam de um provedor de autenticação para processar as solicitações de autenticação.*/
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;

    }


    /* passwordEncoder() é um codificador de senha que é responsável por codificar a senha 
    fornecida pelo usuário durante o processo de autenticação.*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
