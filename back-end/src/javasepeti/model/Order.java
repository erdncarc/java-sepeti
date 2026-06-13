package com.example.javasepeti.model;

import com.example.javasepeti.enums.OrderAssignmentStatus;
import com.example.javasepeti.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.javasepeti.enums.OrderStatus;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @CreationTimestamp
    @Column(name = "date", nullable = false, updatable = false)
    private LocalDateTime date = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.RECEIVED;

    @Column(name = "tot_amount", nullable = false, precision = 10, scale = 2, updatable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(columnDefinition = "TEXT")
    private String note;


    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must not exceed 5.")
    @Digits(integer = 1, fraction = 1, message = "Rating must have only 1 decimal place.")
    @Column(precision = 2, scale = 1, nullable = true)
    private BigDecimal rating;

    @OneToOne
    @JoinColumn(name = "address_id")
    private CusAddress address;

    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "res_id", nullable = false, updatable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @ManyToOne
    @JoinColumn(name = "card_num", nullable = true)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "cus_id", nullable = false, updatable = false)
    @JsonBackReference
    private Customer customer;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Review review;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Refund refund;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_status", nullable = false)
    private OrderAssignmentStatus assignmentStatus = OrderAssignmentStatus.PENDING;


    public Order(Customer customer, Card card, CusAddress address, PaymentMethod method, String note) {
        this.customer = customer;
        this.card = card;
        this.cart = customer.getActiveCart();
        this.totalAmount = getTotalAmount();
        this.restaurant = cart.getRestaurant();
        this.address = address;
        this.totalAmount = cart.getTotalAmount();
        this.paymentMethod = method;
        this.note = note;
    }
}