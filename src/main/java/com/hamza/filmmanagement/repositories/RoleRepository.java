package com.hamza.filmmanagement.repositories;

import com.hamza.filmmanagement.entities.Role;
import com.hamza.filmmanagement.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
