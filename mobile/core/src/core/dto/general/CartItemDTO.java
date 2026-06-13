package com.example.core.dto.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CartItemDTO extends ItemDTO{

    private Long cartItemId;

    private Integer quantity;

    private MenuItemDTO menuItem;
}
