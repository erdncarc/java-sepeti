package com.example.javasepeti.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.example.javasepeti.Utils;
import com.example.javasepeti.dto.*;
import com.example.javasepeti.dto.OpeningHourRequest;
import com.example.javasepeti.dto.auth.MessageResponseDTO;
import com.example.javasepeti.dto.general.MenuItemDTO;
import com.example.javasepeti.dto.general.ReviewDTO;
import com.example.javasepeti.dto.restaurant.OpeningHourDTO;
import com.example.javasepeti.dto.restaurant.RestaurantDTO;
import com.example.javasepeti.dto.restaurant.UpdateRestaurantInfoDTO;
import com.example.javasepeti.enums.AccountStatus;
import com.example.javasepeti.model.MenuItem;
import com.example.javasepeti.model.OpeningHour;
import com.example.javasepeti.service.ReviewService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.javasepeti.model.Restaurant;
import com.example.javasepeti.service.RestaurantService;
import com.example.javasepeti.security.CustomUserDetails;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    private final ModelMapper modelMapper;
    @Autowired
    public RestaurantController(RestaurantService restaurantService, ModelMapper modelMapper,
                                ReviewService reviewService) {
        this.restaurantService = restaurantService;
        this.modelMapper = modelMapper;
        this.reviewService = reviewService;
    }


    @GetMapping("/me")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Restaurant restaurant = restaurantService.findById(userDetails.getId());
        return ResponseEntity.ok(modelMapper.map(restaurant, RestaurantDTO.class));
    }

    @PutMapping("/me")
    public ResponseEntity<RestaurantDTO> updateRestaurantInfo(@RequestBody UpdateRestaurantInfoDTO updateRestaurantInfo, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Restaurant restaurant = restaurantService.updateRestaurantInfo(userDetails.getId(), updateRestaurantInfo);
        return ResponseEntity.ok(modelMapper.map(restaurant, RestaurantDTO.class));
    }

    @PutMapping("/me/photo")
    public ResponseEntity<RestaurantDTO> updateProfilePhoto(@RequestBody String image, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Restaurant restaurant =  restaurantService.updateProfilePhoto(userDetails.getId(), Utils.clearBase64Image(image));
        return ResponseEntity.ok(modelMapper.map(restaurant, RestaurantDTO.class));
    }

    @PostMapping("/me/address")
    public ResponseEntity<RestaurantDTO> addAddress(@RequestBody BusAddressRequest busAddressRequest, @AuthenticationPrincipal CustomUserDetails userDetails){
        Restaurant restaurant = restaurantService.addAddress(userDetails.getId(), busAddressRequest);
        return ResponseEntity.ok(modelMapper.map(restaurant, RestaurantDTO.class));
    }

    @PutMapping("/me/address")
    public ResponseEntity<RestaurantDTO> updateAddress(@RequestBody BusAddressRequest busAddressRequest, @AuthenticationPrincipal CustomUserDetails userDetails){
        Restaurant restaurant =  restaurantService.updateAddress(userDetails.getId(), busAddressRequest);
        return ResponseEntity.ok(modelMapper.map(restaurant, RestaurantDTO.class));
    }



    @PostMapping("/me/menu-items")
    public ResponseEntity<MenuItemDTO> addMenuItem(@RequestBody AddMenuItemRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(request.getImage() != null){
            request.setImage(Utils.clearBase64Image(request.getImage()));
        }
        MenuItem item = restaurantService.addMenuItem(userDetails.getId(), request);
        return ResponseEntity.ok(modelMapper.map(item, MenuItemDTO.class));
    }

    @DeleteMapping("/me/menu-items/{menuItemId}")
    public ResponseEntity<MessageResponseDTO> deleteMenuItem(@PathVariable Long menuItemId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        restaurantService.deleteMenuItem(userDetails.getId(), menuItemId);
        return ResponseEntity.ok(new MessageResponseDTO("Item deleted Successfully"));
    }

    @PutMapping("/me/menu-items")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@RequestBody UpdateMenuItemRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        MenuItem item = restaurantService.updateMenuItem(userDetails.getId(), request);
        return ResponseEntity.ok(modelMapper.map(item, MenuItemDTO.class));
    }

    @GetMapping("/me/menu-items")
    public ResponseEntity<List<MenuItemDTO>> getMenuItems(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<MenuItem> items = restaurantService.getMenuItems(userDetails.getId());
        List<MenuItemDTO> itemDTOs = modelMapper.map(items, new TypeToken<List<MenuItemDTO>>(){}.getType());
        return ResponseEntity.ok(itemDTOs);
    }

    @GetMapping("/me/menu-items/{menuItemId}")
    public ResponseEntity<MenuItemDTO> getMenuItem(@PathVariable Long menuItemId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        MenuItem item = restaurantService.getMenuItem(userDetails.getId(), menuItemId);
        return ResponseEntity.ok(modelMapper.map(item, MenuItemDTO.class));
    }

    @PostMapping("/me/opening-hours")
    public ResponseEntity<RestaurantDTO> setOpeningHours(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestBody @Valid List<@Valid OpeningHourRequest> hours) {
        Restaurant restaurant =  restaurantService.setOpeningHours(userDetails.getId(), hours);
        return ResponseEntity.ok(modelMapper.map(restaurant, RestaurantDTO.class));
    }

    @GetMapping("/me/opening-hours")
    public ResponseEntity<List<OpeningHourDTO>> getOpeningHours(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<OpeningHour> hours = restaurantService.getOpeningHours(userDetails.getId());
        List<OpeningHourDTO> hourDTOS = modelMapper.map(hours, new TypeToken<List<OpeningHourDTO>>(){}.getType());
        return ResponseEntity.ok(hourDTOS);
    }

    @PutMapping("/me/complete-registration")
    public ResponseEntity<RestaurantDTO> completeRegistration(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(modelMapper.map(restaurantService.completeRegistration(userDetails.getId()),RestaurantDTO.class));
    }

    @GetMapping("/restaurant/{restaurantId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviewsByRestaurant(@PathVariable Long restaurantId) {
        List<ReviewDTO> reviews = reviewService.getReviewsForRestaurant(restaurantId);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/restaurant/{restaurantId}/review/{reviewId}/reply")
    public ResponseEntity<String> replyToReview(@PathVariable Long restaurantId, @PathVariable Long reviewId,
                                                @RequestBody String replyText) {

        try {
            reviewService.replyToReview(reviewId, restaurantId, replyText);
            return ResponseEntity.ok("Reply saved");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}