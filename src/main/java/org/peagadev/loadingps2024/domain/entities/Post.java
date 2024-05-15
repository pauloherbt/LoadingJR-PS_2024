package org.peagadev.loadingps2024.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "posts")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
