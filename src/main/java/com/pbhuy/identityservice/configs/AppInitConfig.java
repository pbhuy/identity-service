package com.pbhuy.identityservice.configs;

import com.pbhuy.identityservice.entities.User;
import com.pbhuy.identityservice.enums.Role;
import com.pbhuy.identityservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
public class AppInitConfig {

    private final PasswordEncoder passwordEncoder;

    public AppInitConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User newUser = new User();
                newUser.setUsername("admin");
                newUser.setPassword(passwordEncoder.encode("admin"));
                Set<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());
//                newUser.setRoles(roles);
                userRepository.save(newUser);
                log.warn("admin account has been created with default password is admin, please check and reset it");
            }
        };
    }
}
