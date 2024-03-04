package com.iris.genesis.modules.login.controller;

import com.iris.genesis.modules.login.model.User;
import com.iris.genesis.modules.login.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ResourceController {
    private  final UserRepository userRepository;

    public ResourceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String getResource() {
        return "a value...admin ihany mahite anty";
    }

    @GetMapping("/user")
    public List<User> getResourceUser() {
        return userRepository.findAll();
    }
}
