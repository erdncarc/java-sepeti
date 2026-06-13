package com.example.javasepeti.model;

import com.example.javasepeti.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refId;

    RefundStatus status = RefundStatus.PENDING;

    private String reason;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
} 