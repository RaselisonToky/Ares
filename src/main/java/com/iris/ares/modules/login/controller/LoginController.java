package com.iris.ares.modules.login.controller;

import com.iris.ares.modules.login.model.BasicUser;
import com.iris.ares.modules.login.model.LoginRequest;
import com.iris.ares.modules.login.services.JWTService;
import com.iris.ares.modules.login.services.UserService;
import com.iris.ares.modules.primarykeysGenerator.IdGeneratorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * LoginController
 * Controller class for handling user login and signup endpoints.
 */
@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*")
public class LoginController {
    /**
     * JwtService
     */
    public final JWTService jwtService;
    private final AuthenticationManager authenticationManager ;


    /**
     * Constructor
     * @param userService The UserService for managing user operations.
     * @param jwtService The JWTService for generating JWT tokens.
     * @param authenticationManager The AuthenticationManager for handling authentication.
     */
    public LoginController(UserService userService, IdGeneratorService idGeneratorService, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    /**
     * Login Endpoint
     * Authenticates a user and generates a JWT token upon successful login.
     *
     * @param loginRequest The login request containing username and password.
     * @return A JWT token as a String.
     */
    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );
            System.out.println("Authentication after login: " + authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateToken(authentication);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

}
