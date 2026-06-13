package com.example.javasepeti.model;


import com.example.javasepeti.enums.Label;
import com.example.javasepeti.enums.MealCategory;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long itemId;

    @Column(name = "name", nullable = false)
    protected String name;
    protected String description;

    @ElementCollection(targetClass = MealCategory.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "item_categories", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "category")
    protected List<MealCategory> categories = new ArrayList<>();

    @ElementCollection(targetClass = Label.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "item_labels", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "label")
    protected List<Label> labels = new ArrayList<>();

    @Column(name = "protein", nullable = false)
    protected int protein;

    @Column(name = "carb", nullable = false)
    protected int carb;

    @Column(name = "fat", nullable = false)
    protected int fat;

    @Column(name = "calorie", nullable = false)
    protected int calorie;

    @Column(name = "quantity", nullable = false)
    @PositiveOrZero
    private Integer quantity = 0;

    @Lob
    @Column(nullable = true, columnDefinition = "LONGTEXT")
    private String image;

}