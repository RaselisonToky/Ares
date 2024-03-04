package com.iris.genesis.modules.login.controller;

import com.iris.genesis.modules.login.model.User;
import com.iris.genesis.modules.login.model.request.LoginRequest;
import com.iris.genesis.modules.login.services.JWTService;
import com.iris.genesis.modules.login.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {
    private final UserService userService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager ;

    public LoginController(UserService userService, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public String getToken(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );
            System.out.println("Authentication after login: " + authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return jwtService.generateToken(authentication);
        } catch (UsernameNotFoundException e) {
            return ("Invalid username or password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }
}

