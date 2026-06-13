package com.example.javasepeti.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class DailyNutrition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "daily_nutrition_items", // bu ara tablo olacak
            joinColumns = @JoinColumn(name = "daily_nutrition_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items =  new ArrayList<>();;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "date", nullable = false)
    private LocalDate date;



}
