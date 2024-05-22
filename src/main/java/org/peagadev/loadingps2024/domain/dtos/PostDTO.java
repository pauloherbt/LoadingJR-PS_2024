package org.peagadev.loadingps2024.domain.dtos;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.peagadev.loadingps2024.domain.entities.Post;
import org.peagadev.loadingps2024.enums.PostType;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDTO {

    private String id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostType postType;
    private UserDTO user;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.postType = post.getPostType();
        if(post.getUser() != null)
            user=new UserDTO(post.getUser());
    }
}
