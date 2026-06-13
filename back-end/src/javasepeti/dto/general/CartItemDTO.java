package com.example.javasepeti.dto.general;

import com.example.javasepeti.model.Cart;
import com.example.javasepeti.model.MenuItem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CartItemDTO extends ItemDTO{

    private Long cartItemId;

    private Integer quantity = 0;

    private MenuItemDTO menuItem;
}
