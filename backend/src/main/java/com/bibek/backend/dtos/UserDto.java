package com.bibek.backend.dtos;

import com.bibek.backend.auth.entities.Provider;
import com.bibek.backend.auth.entities.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @JsonProperty("id")
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String image;
    private boolean enable = true;
    private Provider provider = Provider.LOCAL;
    private Set<Role> roles = new HashSet<>();
    private Instant createdAt;
    private Instant updatedAt;
}
