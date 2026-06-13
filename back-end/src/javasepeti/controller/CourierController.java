package com.example.javasepeti.controller;

import com.example.javasepeti.Utils;
import com.example.javasepeti.dto.*;
import com.example.javasepeti.dto.courier.OrderSummaryDTO;
import com.example.javasepeti.dto.courier.RestaurantSummaryDTO;
import com.example.javasepeti.dto.general.OrderDTO;
import com.example.javasepeti.dto.general.ReviewDTO;
import com.example.javasepeti.enums.OrderAssignmentStatus;
import com.example.javasepeti.enums.OrderStatus;
import com.example.javasepeti.model.BusAddress;
import com.example.javasepeti.model.Courier;
import com.example.javasepeti.model.Order;
import com.example.javasepeti.model.Restaurant;
import com.example.javasepeti.security.CustomUserDetails;
import com.example.javasepeti.service.CourierService;
import com.example.javasepeti.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courier")
public class CourierController {

    private final CourierService courierService;

    private final ModelMapper modelMapper;
    private final ReviewService reviewService;
    @Autowired

    public CourierController(CourierService courierService, ModelMapper modelMapper, ReviewService reviewService) {
        this.courierService = courierService;
        this.modelMapper = modelMapper;
        this.reviewService = reviewService;
    }



    @GetMapping("/me")
    public ResponseEntity<Courier> getUserById(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(courierService.findById(userDetails.getId()));
    }

    @PutMapping("/me/status")
    public ResponseEntity<Courier> updateCourierStatus(@RequestBody UpdateCourierStatus updateCourierStatus, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(courierService.updateCourierStatus(userDetails.getId(), updateCourierStatus));
    }

    @PutMapping("/me/photo")
    public ResponseEntity<Courier> updateProfilePhoto(@RequestBody String image, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(courierService.updateProfilePhoto(userDetails.getId(), Utils.clearBase64Image(image)));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestBody CourierUpdateProfile updateDTO,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        Courier updatedCourier = courierService.updateProfile(userDetails.getId(), updateDTO);
        return ResponseEntity.ok(updatedCourier);

    }

    @PostMapping("/me/address")
    public ResponseEntity<Courier> addAddress(@RequestBody BusAddressRequest busAddressRequest, @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(courierService.addAddress(userDetails.getId(), busAddressRequest));
    }

    @PutMapping("/me/address")
    public ResponseEntity<Courier> updateAddress(@RequestBody BusAddressRequest busAddressRequest, @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(courierService.updateAddress(userDetails.getId(), busAddressRequest));
    }

    @GetMapping("/me/addresses")
    public ResponseEntity<BusAddress<Courier>> getAddresses(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(courierService.getAddress(userDetails.getId()));
    }

    @GetMapping("/me/new-restaurants")
    public ResponseEntity<List<RestaurantSummaryDTO>> getNewRestaurants(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Restaurant> restaurants = courierService.getNewRestaurants(userDetails.getId());
        return ResponseEntity.ok(restaurants.stream().map(r-> modelMapper.map(r, RestaurantSummaryDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("/me/registered-restaurants")
    public ResponseEntity<List<RestaurantSummaryDTO>> getRegisteredRestaurants(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Restaurant> restaurants = courierService.getRegisteredRestaurants(userDetails.getId());
        return ResponseEntity.ok(restaurants.stream().map(r-> modelMapper.map(r, RestaurantSummaryDTO.class)).collect(Collectors.toList()));
    }

    @PostMapping("/me/add-restaurant/{restaurantId}")
    public ResponseEntity<String> addRestaurantToCourier(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @PathVariable Long restaurantId) {
        courierService.addRestaurantToCourier(userDetails.getId(), restaurantId);
        return ResponseEntity.ok("Restaurant added to courier successfully");
    }

    @GetMapping("/me/orders/active")
    public ResponseEntity<List<OrderSummaryDTO>> getActiveOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Order> orders = courierService.getActiveOrders(userDetails.getId());
        return ResponseEntity.ok(orders.stream().map(o-> modelMapper.map(o, OrderSummaryDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("/me/orders/previous")
    public ResponseEntity<List<OrderSummaryDTO>> getPreviousOrders(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Order> orders = courierService.getPreviousOrders(userDetails.getId(), date);
        return ResponseEntity.ok(orders.stream().map(o-> modelMapper.map(o, OrderSummaryDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        Order order = courierService.getOrder(orderId);
        return ResponseEntity.ok(modelMapper.map(order, OrderDTO.class));
    }


    @PostMapping("/me/orders/{orderId}/assignment-status")
    public ResponseEntity<Void> updateOrderAssignmentStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable Long orderId,
                                                            @RequestParam OrderAssignmentStatus status) {

        courierService.updateOrderAssignmentStatus(orderId, userDetails.getId(), status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/current-packet")
    public ResponseEntity<OrderSummaryDTO> getCurrentPacket(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Order order = courierService.getCurrentPacket(userDetails.getId());
        if (order == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(modelMapper.map(order, OrderSummaryDTO.class));
    }

    @PostMapping("/me/update-order-status")
    public ResponseEntity<Void> updateOrderStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestParam("status") OrderStatus newStatus) {

        courierService.updateOrderStatus(userDetails.getId(), newStatus);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courier/{courierId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviewsByCourier(@PathVariable Long courierId) {
        List<ReviewDTO> reviews = reviewService.getReviewsForCourier(courierId);
        return ResponseEntity.ok(reviews);
    }

}
