package org.peagadev.loadingps2024.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.peagadev.loadingps2024.enums.PostType;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
public class PostDTO {

    private String id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostType postType;

}
