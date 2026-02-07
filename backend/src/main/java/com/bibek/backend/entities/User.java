package com.bibek.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "users")
@AttributeOverride(
        name = "id",
        column = @Column(name = "user_id")
)
public class User extends BaseEntity {
    @Column(name = "user_name", length = 200)
    private String name;

    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private String image;
    private boolean enable = true;

    @Enumerated(EnumType.STRING)
    private Provider provider = Provider.LOCAL;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
