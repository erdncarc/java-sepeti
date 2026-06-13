package com.example.javasepeti.dto.general;

import com.example.javasepeti.dto.general.ItemDTO;
import com.example.javasepeti.enums.Allergen;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuItemDTO extends ItemDTO {

    private String hashId;

    private BigDecimal price;

    private String image;

    private List<Allergen> allergens;

    private Integer quantity ;

    private BigDecimal rating;

}
