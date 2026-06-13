package com.example.javasepeti.map;

import com.example.javasepeti.dto.courier.OrderSummaryDTO;
import com.example.javasepeti.model.Order;
import org.modelmapper.PropertyMap;

public class OrderToOrderSummaryDTO extends PropertyMap<Order, OrderSummaryDTO> {

    @Override
    protected void configure() {
        map().setOrderId(source.getOrderId());
        map().setRestaurantName(source.getRestaurant().getRestaurantName());
        map().setCustomerName(source.getCustomer().getName());
        map().setAddress(source.getAddress());
        map().setDate(source.getDate());
        map().setStatus(source.getStatus());
    }
}
