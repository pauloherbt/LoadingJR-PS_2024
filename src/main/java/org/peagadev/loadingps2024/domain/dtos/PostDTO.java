package org.peagadev.loadingps2024.domain.dtos;

import lombok.Getter;
import lombok.Setter;
import org.peagadev.loadingps2024.enums.PostType;
@Getter
@Setter
public class PostDTO {

    private String id;
    private String title;
    private String description;

    private PostType postType;

}
