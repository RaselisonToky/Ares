package com.iris.ares.modules.login.services;

import com.iris.ares.modules.login.Repository.BasicUserRepository;
import com.iris.ares.modules.login.model.BasicUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * UserService
 * Service class for managing user-related operations.
 * This service provides methods for saving user data to the database.
 */
@Service
public class UserService {
    private final BasicUserRepository basicUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor
     * @param basicUserRepository The repository for accessing BasicUser data.
     * @param passwordEncoder The password encoder for encoding user passwords.
     */
    public UserService(BasicUserRepository basicUserRepository, BCryptPasswordEncoder passwordEncoder) {
        this.basicUserRepository = basicUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Save User
     * Saves the provided user to the database after encoding the password.
     * @param user The user object to be saved.
     */
    public void saveUser(BasicUser user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        basicUserRepository.save(user);
    }

}
