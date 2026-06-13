package com.example.javasepeti.dto.customer;

import lombok.Data;

@Data
public class RefundRequestDTO {

    String reason;

    Long orderId;
}
