package com.example.javasepeti.config;

import com.example.javasepeti.map.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.addMappings(new BusAddressToBusAddressDTO<>());
        modelMapper.addMappings(new ReviewToReviewDTO());
        modelMapper.addMappings(new RefundToRefundDTO());
        modelMapper.addMappings(new RestaurantToRestaurantInfoDTO());
        modelMapper.addMappings(new CartItemToCartItemDTO());

        modelMapper.addMappings(new CartToCartResponseDTOMap());
        modelMapper.addMappings(new CusAddressToCusAddressDTO());
        modelMapper.addMappings(new CardToCardResponseDTO());

        modelMapper.addMappings(new RestaurantToRestaurantSummaryDTO());
        modelMapper.addMappings(new OrderToOrderSummaryDTO());

        return modelMapper;
    }
}

