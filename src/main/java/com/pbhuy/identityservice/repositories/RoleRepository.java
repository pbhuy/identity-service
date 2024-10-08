package com.pbhuy.identityservice.repositories;

import com.pbhuy.identityservice.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByName(String name);
    boolean existsByName(String name);
}
