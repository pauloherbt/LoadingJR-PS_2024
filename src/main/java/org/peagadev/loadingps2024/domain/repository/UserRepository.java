package org.peagadev.loadingps2024.domain.repository;

import org.peagadev.loadingps2024.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
