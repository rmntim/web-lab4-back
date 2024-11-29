package ru.rmntim.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExtendedUserDTO {
    private String username;
    private String password;
    private String email;
    private Long userId;
}
