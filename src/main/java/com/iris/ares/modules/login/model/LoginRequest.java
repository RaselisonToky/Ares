package com.iris.ares.modules.login.model;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;


/**
 * Represents a login request.
 * This class encapsulates the username or email and password submitted during a login attempt.
 */
@Setter
@Getter
public class LoginRequest {
    String username;
    String password;

    /**
     * Default Constructor
     */
    public LoginRequest(){

    }

}
