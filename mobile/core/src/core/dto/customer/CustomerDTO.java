package com.example.core.dto.customer;

import com.example.core.dto.general.UserResponseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerDTO extends UserResponseDTO {

    private Double loyPoint;

    private Integer calLimit;
}
