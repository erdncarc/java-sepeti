package com.example.javasepeti.model;

import com.example.javasepeti.enums.AccountStatus;
import com.example.javasepeti.enums.CartStatus;
import com.example.javasepeti.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@EqualsAndHashCode(callSuper = true) //@Data aynı işlevi görüyor fakat üst sınıfı çağırmıyor. User için buna gerek yok.
@NoArgsConstructor
@AllArgsConstructor
@Data
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    @Column(name = "loy_point", nullable = false)
    @PositiveOrZero
    private Double loyPoint = 0.0;

    @Column(name = "cal_limit")
    @PositiveOrZero
    private Integer calLimit;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<Cart> carts;

    @Transient
    private Cart activeCart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders =  new ArrayList<>();


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews =  new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyNutrition> dailyNutritionList =  new ArrayList<>();;


    @ManyToMany
    @JoinTable(
            name = "customer_favorite_restaurants",  // Bu tabloyu adlandırıyoruz
            joinColumns = @JoinColumn(name = "customer_id"),  // Customer'a ait olan kolon
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")  // Restaurant'a ait olan kolon
    )
    private List<Restaurant> favoriteRestaurants = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CusAddress> addresses = new ArrayList<>();


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();



    public Cart getActiveCart(){
        return carts.stream()
                .filter(c -> c.getCartStatus() == CartStatus.ACTIVE)
                .findFirst()
                .orElse(null);
    }
    @PrePersist
    public void assignRole() {
        this.setRole(UserRole.CUSTOMER);
        this.setAccountStatus(AccountStatus.REGISTER);
    }
} 