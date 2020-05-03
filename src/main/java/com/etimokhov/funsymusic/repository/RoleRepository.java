package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
