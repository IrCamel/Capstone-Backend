package com.progetto.personale.capstone.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
}
