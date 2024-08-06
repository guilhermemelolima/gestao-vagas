package br.com.guilherme.gestao_vagas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private SecurityCompanyFilter securityCompanyFilter;

    @Autowired
    private SecurityCandidateFilter securityCandidateFilter;

    // forma de liberar ou bloquear multiplos endpoints
    private static final String[] PERMIT_ALL_LIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/actuator/**"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
         * O csrf.disable desabilita a autenticação padrão do SpringSecurityUsuário ou senha incorreto
        */
        http.csrf(csrf -> csrf.disable())
                /*
                 * O authorizeHttpRequests nos permite configurar as rotas que são publicas e autenticadas
                */
                .authorizeHttpRequests(auth -> {

                    auth.requestMatchers("/candidate/").permitAll();
                    auth.requestMatchers("/candidate/auth").permitAll();
                    auth.requestMatchers("/candidate/profile").permitAll();

                    auth.requestMatchers("/company/").permitAll();
                    auth.requestMatchers("/company/auth").permitAll();

                    auth.requestMatchers(PERMIT_ALL_LIST).permitAll();


                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(securityCandidateFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(securityCompanyFilter, BasicAuthenticationFilter.class)
        ;
        return http.build();
    }

    /*
    * Utilizando o @Bean o Spring endende que quando usamo sum @Autowired utilizaremos
    * a códificação abaixo onde é retornado o BCryptPasswprdEncoder
    * */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
