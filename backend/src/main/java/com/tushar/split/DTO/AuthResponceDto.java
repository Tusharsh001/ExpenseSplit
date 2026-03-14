package com.tushar.split.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponceDto {

    private String token;
    private String username;


}
