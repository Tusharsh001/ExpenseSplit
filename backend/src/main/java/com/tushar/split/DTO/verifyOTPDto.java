package com.tushar.split.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class verifyOTPDto {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String otp;
}
