package com.example.javasepeti.model;

import com.example.javasepeti.dto.CartItemResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart items")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @Column(name = "quantity", nullable = false)
    @Positive
    private Integer quantity = 0;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false, updatable = false)
    @JsonBackReference
    private Cart cart;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId", updatable = false)
    private MenuItem menuItem;


}