package com.iris.ares.modules.login.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents a basic user in the system.
 * This class provides basic user attributes such as <b>firstName</b> ,
 *  <b>lastName</b> , <b>email</b> , <b>username</b> , <b>password</b> and <b>roles</b> assigned to the user.
 * Users extending this class can add additional attributes as needed.
 * <p>Users extending this class inherit these attributes and can be further customized as needed.
 * Authentication and login functionalities are automatically supported.</p>
 */


@Setter
@Getter
@Entity
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public class BasicUser {
    @Id
    String userId;
    String firstName;
    String lastName;
    String email;
    String username;
    String password;
    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    List<Role> roles;

    /**
     * Default Constructor
     */
    public BasicUser(){

    }

}
