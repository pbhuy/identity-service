package com.pbhuy.identityservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role {

    @Id
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
            @JoinTable(
                    name = "roles_permissions",
                    joinColumns = @JoinColumn(name = "role_name"),
                    inverseJoinColumns = @JoinColumn(name = "permission_name")
            )
    private Set<Permission> permissions;
}
