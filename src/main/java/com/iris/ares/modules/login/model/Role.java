package com.iris.ares.modules.login.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a role in the system.
 * This class defines the role of a user within the system.
 */
@Setter
@Getter
@Entity
public class Role {
    @Id
    String roleId;
    String role;

    /**
     * Default Role
     */
    public Role(){

    }

}
