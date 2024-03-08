package com.iris.ares.modules.login.Repository;

import com.iris.ares.modules.login.model.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BasicUserRepository
 */
public interface BasicUserRepository extends JpaRepository<BasicUser,Integer> {

    /**
     *Method who found the user with
     * @param email and
     * @param username  and  username
     * @return BasicUser
     */
    BasicUser findByEmailOrUsername(String email, String username);
}
