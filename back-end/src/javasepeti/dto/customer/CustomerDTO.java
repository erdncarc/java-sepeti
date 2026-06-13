package com.example.javasepeti.dto.customer;

import com.example.javasepeti.dto.general.UserResponseDTO;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerDTO extends UserResponseDTO {

    @PositiveOrZero
    private Double loyPoint = 0.0;

    @PositiveOrZero
    private Integer calLimit;
}
