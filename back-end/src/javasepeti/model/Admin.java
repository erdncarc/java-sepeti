package com.example.javasepeti.model;

import com.example.javasepeti.enums.AccountStatus;
import com.example.javasepeti.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    @PrePersist
    public void assignRole() {
        this.setRole(UserRole.ADMIN);
        this.setAccountStatus(AccountStatus.AVAILABLE);
    }
} 