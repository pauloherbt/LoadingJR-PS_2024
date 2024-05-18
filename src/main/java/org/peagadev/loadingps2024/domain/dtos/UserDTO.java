package org.peagadev.loadingps2024.domain.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.peagadev.loadingps2024.domain.entities.User;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String id;
    private String name;
    private String email;
    private String password;

    public UserDTO(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
    }


}
