package br.com.jzbreno.config;

import br.com.jzbreno.security.JwtTokenFilter;
import br.com.jzbreno.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtTokenProvider tokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.tokenProvider = jwtTokenProvider;
    }
    //metodo que gera o token, @Bean retorna um objeto que o spring tem que gerenciar
    @Bean
    PasswordEncoder passwordEncoder(){
        //(), COMPRIMENTO DA CHAVE, NUMERO DE VZS QUE VAI SER APLICADO, ALGORITMO DE HASH
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        //
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);
        // monta o id que vai buscar o encoder e insere a logica pbkdf montado anteriormente
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        //seta o encoder montado
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder;
    }

    // todo implementar beans para inserir quais endpoins nao precisa de autenticacao
    // e quais precisam de autenticação

    @Bean
    AuthenticationManager authenticationManger(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
        // @formatter:off
        return http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/person/v1/**", //remover apos testar
                                "/auth/signin"
                        ,"/auth/refresh/**"
                        ,"/swagger-ui/**"
                        ,"/v3/api-docs/**"
                        ,"/auth/createUser").permitAll()
                        .requestMatchers("/api/**").authenticated()// so pode permitir com autheticated
                        .requestMatchers("/users").denyAll() // dependendo da forma que config jpa, pode mostrar as entidades como endpoints, assim podendo chamar user, sempre manter disable()

                )
                .cors(cors -> {})
                .build();
        // @formatter:on
    }
}