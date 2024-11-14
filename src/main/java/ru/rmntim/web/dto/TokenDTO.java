package ru.rmntim.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDTO {
    private String token;

    public TokenDTO(String token) {
        this.token = token;
    }
}

