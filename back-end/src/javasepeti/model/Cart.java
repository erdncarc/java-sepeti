package com.example.javasepeti.model;

import com.example.javasepeti.enums.CartStatus;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "cus_id", nullable = false)
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    private CartStatus cartStatus = CartStatus.ACTIVE;


    @ManyToOne
    @JoinColumn(name = "res_id", nullable = true)
    private Restaurant restaurant;



    public BigDecimal getTotalAmount(){
        BigDecimal price = new BigDecimal(0);
        for (CartItem item : cartItems){
            price = price.add(item.getMenuItem().getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        return price;
    }
}