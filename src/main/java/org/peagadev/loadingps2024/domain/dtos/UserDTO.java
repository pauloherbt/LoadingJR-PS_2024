package org.peagadev.loadingps2024.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.peagadev.loadingps2024.domain.entities.User;
import org.peagadev.loadingps2024.validations.UserEmailValidation;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String id;
    @NotBlank
    private String name;
    @NotBlank @Email @UserEmailValidation
    private String email;
    @NotBlank @Length(min = 6)
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDTO(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
            createdAt = user.getCreatedAt();
            updatedAt = user.getUpdatedAt();
    }


}
