package org.peagadev.loadingps2024.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.peagadev.loadingps2024.domain.entities.Post;
import org.peagadev.loadingps2024.enums.PostType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {

    private String id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @NotNull
    private PostType postType;
    private String imgUrl;
    private UserDTO user;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.postType = post.getPostType();
        this.imgUrl = post.getImgUrl();
        if(post.getUser() != null)
            user=new UserDTO(post.getUser());
    }
}
