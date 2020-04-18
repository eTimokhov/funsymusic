package com.etimokhov.funsymusic.repository;

import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
