package com.example.javasepeti.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CustomerUpdateInfoRequest {

    @NotBlank(message = "Name must not be empty.")
    private String name;
    @Email(message = "Please enter a valid email address.")
    @NotBlank(message = "Email must not be empty.")
    private String email;

    @NotBlank(message = "Phone number must not be empty.")
    @Pattern(regexp = "^5\\d{9}$", message = "Phone number must start with 5 and contain exactly 10 digits.")
    private String phone;

}
