package org.peagadev.loadingps2024.domain.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class RespLoginDto {

    private String username;
    private Date expiresAt;
    private String token;

}
