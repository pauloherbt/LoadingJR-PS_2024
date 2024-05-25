package org.peagadev.loadingps2024.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class LoginDto {

    @NotBlank @Email
    private String email;
    @NotBlank @Length(min = 6)
    private String password;

}
