package com.iris.ares.modules.login.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.iris.ares.reactGenerator.react_handler.ReactProjectHandler.getValueFromEnv;

/**
 * Configuration class for Spring Security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {
    private final String jwtKey = generateSecurityKey();
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Constructor
     * @param customUserDetailsService The custom user details service.
     */
    @Autowired
    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService){
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Provides the AuthenticationManager bean.
     * @param http HttpSecurity object
     * @param bCryptPasswordEncoder BCrypt password encoder
     * @return AuthenticationManager bean
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    /**
     * Provides the SecurityFilterChain bean.
     * @param http HttpSecurity object
     * @return SecurityFilterChain bean
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .anonymous(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->{
                String permitAllRequestMatcher = getValueFromEnv("PUBLIC_ENDPOINTS");
                String[] endpoints = permitAllRequestMatcher.split(",");
                for (String endpoint : endpoints) {
                    auth.requestMatchers("api/v1/auth/*/**", endpoint.trim()).permitAll();
                }
                auth.anyRequest().authenticated();
            })
            .csrf(AbstractHttpConfigurer::disable)
            .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt
                            .jwtAuthenticationConverter(new CustomAuthenticationConverter())
                    ));
        return http.build();
    }


    /**
     * CustomAuthenticationConverter
     * Converts a Jwt token into an AbstractAuthenticationToken.
     */
    static class CustomAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        public AbstractAuthenticationToken convert(Jwt jwt) {
            Collection<String> authorities = jwt.getClaimAsStringList("roles");
            Collection<GrantedAuthority> grantedAuthorities = authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new JwtAuthenticationToken(jwt, grantedAuthorities);
        }
    }


    /**
     * Generates a random security key.
     * @return A random security key
     */
    public String generateSecurityKey() {
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    /**
     * Provides the JwtDecoder bean.
     * @return JwtDecoder bean
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(this.jwtKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName()))
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }


    /**
     * Provides the JwtEncoder bean.
     * @return JwtEncoder bean
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Provides the BCryptPasswordEncoder bean.
     * @return BCryptPasswordEncoder bean
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}