package com.example.javasepeti.map;

import com.example.javasepeti.dto.general.ReviewDTO;
import com.example.javasepeti.model.Review;
import org.modelmapper.PropertyMap;

public class ReviewToReviewDTO extends PropertyMap<Review, ReviewDTO> {
    @Override
    protected void configure() {
        map().setOrderId(source.getOrder().getOrderId());
        map().setMenuItemName(source.getMenuItem().getName());
        map().setCustomerUsername(source.getCustomer().getName());
    }
}
