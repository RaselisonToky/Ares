package com.iris.ares.modules.login.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWTService
 * Service class for generating JSON Web Tokens (JWT) for authentication.
 * This service generates JWT tokens based on the provided authentication information.
 */
@Service
public class JWTService {
    private final JwtEncoder jwtEncoder;

    /**
     * Constructor
     * @param jwtEncoder The JWT encoder used for encoding tokens.
     */
    @Autowired
    public JWTService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Generate Token
     * Generates a JWT token based on the provided authentication information.
     * @param authentication The authentication object containing user details.
     * @return A JWT token as a String.
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofHours(1)))
                .subject(authentication.getName())
                .claim("roles",roles)
                .build();
        System.out.println(claims.getClaims());
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
