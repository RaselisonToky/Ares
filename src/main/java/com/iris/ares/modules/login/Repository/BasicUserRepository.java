package com.iris.ares.modules.login.Repository;

import com.iris.ares.modules.login.model.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicUserRepository extends JpaRepository<BasicUser,Integer> {
    BasicUser findByEmailOrUsername(String email, String username);
}
