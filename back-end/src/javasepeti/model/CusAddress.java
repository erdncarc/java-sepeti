package com.example.javasepeti.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "customer_addresses")
@NoArgsConstructor
@AllArgsConstructor

public class CusAddress extends BaseAddress<Customer> {

    @Column(nullable = false)
    private String title;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false, unique = false)
    @JsonBackReference
    protected Customer user;

}