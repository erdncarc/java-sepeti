package com.example.javasepeti.model;

import com.example.javasepeti.dto.BusAddressRequest;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "business_addresses")
@NoArgsConstructor
@AllArgsConstructor
public class BusAddress<T extends User> extends BaseAddress<T> {

    @Column(name = "range_value", nullable = false)
    @Min(value = 1, message = "Range must be a positive number.")
    private Integer rangeValue;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false, unique = false)
    @JsonBackReference
    protected T user;
}