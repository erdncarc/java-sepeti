package com.example.javasepeti.model;

import com.example.javasepeti.dto.UpdateCourierStatus;
import com.example.javasepeti.enums.AccountStatus;
import com.example.javasepeti.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.javasepeti.enums.CourierStatus;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("COURIER")
public class Courier extends User {

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must not exceed 5.")
    @Digits(integer = 1, fraction = 1, message = "Rating must have only 1 decimal place.")
    private BigDecimal rating = new BigDecimal(0);
    @Enumerated(EnumType.STRING)
    private CourierStatus status = CourierStatus.OFFLINE;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private BusAddress<Courier> address;

    @OneToMany(mappedBy = "courier")
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "couriers")
    private List<Restaurant> restaurants = new ArrayList<>();

    @PrePersist
    public void assignRole() {
        this.setRole(UserRole.COURIER);
        this.setAccountStatus(AccountStatus.REGISTER);
    }

}