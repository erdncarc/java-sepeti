package com.example.core.dto.general;

import com.example.core.model.Location;

import java.util.List;

import lombok.Data;

@Data
public class AddressDTO {
    protected Long addressId;

    protected String street;
    protected String city;

    protected String town;

    protected String apartment;

    protected Integer number;

    protected String details;

    protected Location location;

    public String addressToString(){
        return String.join(" ",city,town,street,apartment,String.valueOf(number));
    }


}
