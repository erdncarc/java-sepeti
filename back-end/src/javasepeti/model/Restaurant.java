package com.example.javasepeti.model;

import com.example.javasepeti.enums.AccountStatus;
import com.example.javasepeti.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurants")
@EqualsAndHashCode(callSuper = true) //@Data aynı işlevi görüyor fakat üst sınıfı çağırmıyor. User için buna gerek yok.
@NoArgsConstructor
@AllArgsConstructor
@Data
@DiscriminatorValue("RESTAURANT")
public class Restaurant extends User {

    private String restaurantName;
    @Embedded
    @Column(precision = 2, scale = 1, nullable = true)
    private Rating rating;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Min(value = 0, message = "Fee ratio must be between 0 and 100.")
    @Max(value = 100, message = "Fee ratio must be between 0 and 100.")
    @Column(nullable = true)
    private Integer feeRatio;

    @Min(value = 0, message = "LP ratio must be between 0 and 100.")
    @Max(value = 100, message = "LP ratio must be between 0 and 100.")
    @Column(nullable = true)
    private Integer lpRatio;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems =  new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Order> orders =  new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private BusAddress<Restaurant> busAddress;

    @Column(nullable = true)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "restaurant_couriers",
            joinColumns = @JoinColumn(name = "res_id"),
            inverseJoinColumns = @JoinColumn(name = "courier_id")
    )
    private List<Courier> couriers = new ArrayList<>();


    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpeningHour> openingHours = new ArrayList<>();


    @PrePersist
    public void assignRole() {
        this.setRole(UserRole.RESTAURANT);
        this.setAccountStatus(AccountStatus.REGISTER);
    }


    public boolean isOpen() {
        if (this.openingHours == null || this.openingHours.isEmpty()) {
            return true; // No opening hours = assume open
        }

        LocalDateTime now = LocalDateTime.now(); // Current time
        DayOfWeek today = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime();

        return openingHours.stream()
                .filter(hour -> hour.getDayOfWeek() == today)
                .anyMatch(hour -> {
                    LocalTime open = hour.getOpenTime();
                    LocalTime close = hour.getCloseTime();
                    return !currentTime.isBefore(open) && !currentTime.isAfter(close);
                });
    }
} 