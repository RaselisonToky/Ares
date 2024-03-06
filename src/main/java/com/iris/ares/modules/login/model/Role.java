package com.iris.ares.modules.login.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Role {
    @Id
    int roleId;
    String role;
}
