package com.iris.ares.modules.login.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public class BasicUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int userId;
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
}
