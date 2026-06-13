package com.example.javasepeti.model;

import com.example.javasepeti.enums.AccountStatus;
import com.example.javasepeti.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"})
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @NotBlank
    @Email(message = "Please provide a valid email address.")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email format is invalid."
    )
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    @Column(nullable = false)
    private String password;

    @Pattern(regexp = "^5\\d{9}$", message = "Phone number must start with 5 and contain exactly 10 digits.")
    @Column(nullable = true)
    private String phone;

    @Lob
    @Column(nullable = true , columnDefinition = "LONGTEXT")
    private String image;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
}