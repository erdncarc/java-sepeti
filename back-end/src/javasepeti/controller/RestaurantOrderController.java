package com.example.javasepeti.controller;
import com.example.javasepeti.dto.general.CourierInfoDTO;
import com.example.javasepeti.enums.OrderStatus;
import com.example.javasepeti.model.Courier;
import com.example.javasepeti.model.Order;
import com.example.javasepeti.security.CustomUserDetails;
import com.example.javasepeti.service.RestaurantOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurant/orders")
public class RestaurantOrderController {

    private final RestaurantOrderService restaurantOrderService;
    private final ModelMapper modelMapper;
    @Autowired
    public RestaurantOrderController(RestaurantOrderService restaurantOrderService, ModelMapper modelMapper) {
        this.restaurantOrderService = restaurantOrderService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/{orderId}/accept")
    public ResponseEntity<Order> acceptOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(restaurantOrderService.acceptOrder(orderId));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(restaurantOrderService.cancelOrder(orderId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(restaurantOrderService.updateStatus(orderId, status));
    }

    @GetMapping("/available-for-assignment")
    public ResponseEntity<List<CourierInfoDTO>> getAvailableCouriers(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Courier> couriers =  restaurantOrderService.getAvailableCouriersForAssignment(userDetails.getId());
        return ResponseEntity.ok(couriers.stream().map(c -> modelMapper.map(c, CourierInfoDTO.class)).collect(Collectors.toList())) ;
    }

    @PostMapping("/assign-courier")
    public ResponseEntity<String> assignCourierToOrder(@RequestParam Long orderId, @RequestParam Long courierId) {
        restaurantOrderService.assignCourierToOrder(orderId, courierId);
        return ResponseEntity.ok("Courier successfully assigned to order.");
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId,
            @RequestParam OrderStatus newStatus
    ) {
        restaurantOrderService.updateOrderStatus(userDetails.getId(), orderId, newStatus);
        return ResponseEntity.ok("Order status updated successfully.");
    }

}
