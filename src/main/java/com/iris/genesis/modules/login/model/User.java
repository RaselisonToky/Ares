package com.iris.genesis.modules.login.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "utilisateur")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int UserId;
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
