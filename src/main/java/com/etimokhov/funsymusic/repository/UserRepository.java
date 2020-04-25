package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
