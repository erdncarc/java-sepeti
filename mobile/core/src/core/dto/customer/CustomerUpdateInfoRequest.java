package com.example.core.dto.customer;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CustomerUpdateInfoRequest {

    private String name;
    private String email;

    private String phone;

}
