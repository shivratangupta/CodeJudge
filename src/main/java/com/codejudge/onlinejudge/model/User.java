package com.codejudge.onlinejudge.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends Auditable {

    @NotBlank
    private String name;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String saltedHashedPassword;

    private boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
