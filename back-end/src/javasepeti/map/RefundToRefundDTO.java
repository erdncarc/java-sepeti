package com.example.javasepeti.map;

import com.example.javasepeti.dto.general.RefundDTO;
import com.example.javasepeti.model.Refund;
import org.modelmapper.PropertyMap;

public class RefundToRefundDTO extends PropertyMap<Refund, RefundDTO> {

    @Override
    protected void configure() {
        map().setOrderId(source.getOrder().getOrderId());
    }
}
