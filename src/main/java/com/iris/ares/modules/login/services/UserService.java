package com.iris.ares.modules.login.services;

import com.iris.ares.modules.login.Repository.BasicUserRepository;
import com.iris.ares.modules.login.model.BasicUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final BasicUserRepository basicUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(BasicUserRepository basicUserRepository, BCryptPasswordEncoder passwordEncoder) {
        this.basicUserRepository = basicUserRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void saveUser(BasicUser user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        basicUserRepository.save(user);
    }

}
