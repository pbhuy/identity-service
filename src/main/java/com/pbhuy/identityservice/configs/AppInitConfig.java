package com.pbhuy.identityservice.configs;

import com.pbhuy.identityservice.entities.Permission;
import com.pbhuy.identityservice.entities.User;
import com.pbhuy.identityservice.entities.Role;
import com.pbhuy.identityservice.repositories.PermissionRepository;
import com.pbhuy.identityservice.repositories.RoleRepository;
import com.pbhuy.identityservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
public class AppInitConfig {
    private final String ADMIN = "admin";

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppInitConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository,
                                               RoleRepository roleRepository,
                                               PermissionRepository permissionRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                initialRole(roleRepository, permissionRepository);
            }
            if (userRepository.findByUsername(ADMIN).isEmpty()) {
                User newUser = new User();
                newUser.setUsername(ADMIN);
                newUser.setPassword(passwordEncoder.encode(ADMIN));
                Role role = roleRepository.findByName(ADMIN.toUpperCase());
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                newUser.setRoles(roles);
                userRepository.save(newUser);
                log.warn("Admin account has been created with default password is admin, please check and reset it");
            }
        };
    }

    private void initialRole(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        Permission adminPermission = Permission.builder()
                .name("APPROVE_POST")
                .description("approve a post")
                .build();
        permissionRepository.save(adminPermission);
        Permission userPermission = Permission.builder()
                .name("UPDATE_USER")
                .description("update user data")
                .build();
        permissionRepository.save(userPermission);
        Set<Permission> adminPermissions = new HashSet<>();
        adminPermissions.add(adminPermission);
        Set<Permission> userPermissions = new HashSet<>();
        userPermissions.add(userPermission);
        Role adminRole = Role.builder()
                .name(ADMIN.toUpperCase())
                .description("Admin role")
                .permissions(adminPermissions)
                .build();
        Role userRole = Role.builder()
                .name("USER")
                .description("User role")
                .permissions(userPermissions)
                .build();
        roleRepository.save(adminRole);
        roleRepository.save(userRole);
        log.warn("Initial role has been created");
    }
}
