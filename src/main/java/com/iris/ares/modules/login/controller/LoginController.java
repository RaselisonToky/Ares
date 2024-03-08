package com.iris.ares.modules.login.controller;

import com.iris.ares.modules.login.model.BasicUser;
import com.iris.ares.modules.login.model.LoginRequest;
import com.iris.ares.modules.login.services.JWTService;
import com.iris.ares.modules.login.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;



/**
 * LoginController
 * Controller class for handling user login and signup endpoints.
 */
@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*")
public class LoginController {
    private final UserService userService;
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
    public LoginController(UserService userService, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    /**
     * Login Endpoint
     * Authenticates a user and generates a JWT token upon successful login.
     * @param loginRequest The login request containing username and password.
     * @return A JWT token as a String.
     */
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


    /**
     * Signup Endpoint
     * Registers a new user in the system.
     * @param user The user object to be registered.
     * @return ResponseEntity with status and message indicating success or failure.
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody BasicUser user) {
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }
}
