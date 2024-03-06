package com.iris.ares.modules.login.config;

import com.iris.ares.modules.login.Repository.BasicUserRepository;
import com.iris.ares.modules.login.model.BasicUser;
import com.iris.ares.modules.login.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final BasicUserRepository basicUserRepository;

    @Autowired
    public CustomUserDetailsService(BasicUserRepository basicUserRepository) {
        this.basicUserRepository = basicUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        BasicUser user = basicUserRepository.findByEmailOrUsername(emailOrUsername,emailOrUsername);
        return new User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user.getRoles()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole())));
        return authorities;
    }
}
