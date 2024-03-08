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


/**
 * CustomUserDetailsService
 * Custom implementation of Spring Security's UserDetailsService interface.
 * This service loads user details from the database and provides them to Spring Security for authentication and authorization purposes.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final BasicUserRepository basicUserRepository;

    /**
     * Constructor
     * @param basicUserRepository The repository for accessing BasicUser data.
     */
    @Autowired
    public CustomUserDetailsService(BasicUserRepository basicUserRepository) {
        this.basicUserRepository = basicUserRepository;
    }


    /**
     * Load user by username or email.
     * This method loads user details from the database based on the provided username or email.
     * @param emailOrUsername The email address or username of the user.
     * @return UserDetails object containing user details for authentication.
     * @throws UsernameNotFoundException if the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        BasicUser user = basicUserRepository.findByEmailOrUsername(emailOrUsername,emailOrUsername);
        return new User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user.getRoles()));
    }


    /**
     * Get Granted Authorities.
     * This method retrieves the list of authorities (roles) associated with the user.
     * @param roles The roles assigned to the user.
     * @return List of GrantedAuthority objects representing the user's roles.
     */
    private List<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole())));
        return authorities;
    }
}
