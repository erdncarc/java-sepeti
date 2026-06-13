package com.example.javasepeti.dto.general;

import lombok.Data;

@Data
public class RefundDTO {

    private Long refId;

    private String reason;

    private Long orderId;
}
