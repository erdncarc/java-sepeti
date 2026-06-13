package com.example.javasepeti.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@MappedSuperclass
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "addressId")
public abstract class BaseAddress<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long addressId;

    protected String street;

    @Column(name = "city", nullable = false)
    protected String city;

    @Column(name = "town", nullable = false)
    protected String town;

    protected String apartment;

    @Column(name = "number", nullable = false)
    @Min(value = 0, message = "Number must be a non-negative integer.")
    protected Integer number;

    protected String details;

    @NotNull
    @Embedded
    protected Location location;

}