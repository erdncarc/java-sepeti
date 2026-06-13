package com.example.javasepeti.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rev_id;

    @Embedded
    private Rating rating;
    private String comment;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;


    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;


    @ManyToOne(optional = false) // not nullable
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(length = 500)
    private String reply;

    private Date replyDate;


} 