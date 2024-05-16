package org.peagadev.loadingps2024.domain.repository;

import org.peagadev.loadingps2024.domain.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post,String> {
}
