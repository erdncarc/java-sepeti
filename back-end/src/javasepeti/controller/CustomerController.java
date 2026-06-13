package com.example.javasepeti.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.javasepeti.Utils;
import com.example.javasepeti.dto.*;
import com.example.javasepeti.dto.auth.MessageResponseDTO;
import com.example.javasepeti.dto.customer.*;
import com.example.javasepeti.dto.general.MenuItemDTO;
import com.example.javasepeti.dto.general.OrderDTO;
import com.example.javasepeti.dto.general.RestaurantInfoDTO;
import com.example.javasepeti.dto.general.ReviewDTO;
import com.example.javasepeti.model.*;
import com.example.javasepeti.security.CustomUserDetails;
import com.example.javasepeti.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.javasepeti.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customer")

public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;
    private final ReviewService reviewService;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper modelMapper, ReviewService reviewService) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
        this.reviewService = reviewService;
    }


    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantInfoDTO>> getAllRestaurants(@RequestParam Long addressId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<Restaurant> restaurants = customerService.getAllRestaurants(addressId, userDetails.getId());
        return ResponseEntity.ok(restaurants.stream().map(r-> modelMapper.map(r, RestaurantInfoDTO.class)).collect(Collectors.toList()));
    }



    @GetMapping("/me")
    public ResponseEntity<CustomerDTO> getUserById(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Customer customer = customerService.findById(userDetails.getId());
        CustomerDTO dto = modelMapper.map(customer, CustomerDTO.class);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me/cart")
    public ResponseEntity<CartResponseDTO> getCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Cart cart = customerService.getCart(userDetails.getId());
        return ResponseEntity.ok(modelMapper.map(cart, CartResponseDTO.class));
    }

    @PutMapping("/me")
    public ResponseEntity<CustomerDTO> updateProfileInfo(@Valid @RequestBody CustomerUpdateInfoRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Customer customer = customerService.updateProfileInfo(userDetails.getId(), request);
        return ResponseEntity.ok(modelMapper.map(customer, CustomerDTO.class));
    }

    @PutMapping("/me/photo")
    public ResponseEntity<CustomerDTO> updateProfilePhoto(@Valid @RequestBody String image, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Customer customer  =customerService.updateProfilePhoto(userDetails.getId(), Utils.clearBase64Image(image));
        return ResponseEntity.ok(modelMapper.map(customer, CustomerDTO.class));
    }

    @PostMapping("/me/addresses")
    public ResponseEntity<CusAddressResponseDTO> addAddress(@Valid @RequestBody AddCusAddressRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {

        CusAddress address =  customerService.addAddressToCustomer(userDetails.getId(), request);
        return ResponseEntity.ok(modelMapper.map(address, CusAddressResponseDTO.class) );
    }

    @DeleteMapping("/me/addresses")
    public ResponseEntity<MessageResponseDTO> deleteAddress(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long addressId) {
        customerService.deleteAddressFromCustomer(userDetails.getId(), addressId);
        return ResponseEntity.ok(new MessageResponseDTO("Address deleted successfully."));
    }

    @GetMapping("/me/addresses")
    public ResponseEntity<List<CusAddressResponseDTO>> getAddresses(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(customerService.getAddresses(userDetails.getId()).stream().map(adr -> modelMapper.map(adr, CusAddressResponseDTO.class)).collect(Collectors.toList()));
    }

    @PutMapping("/me/addresses")
    public ResponseEntity<CusAddressResponseDTO> updateAddress(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody UpdateCusAddressRequest request) {
        return ResponseEntity.ok(modelMapper.map(customerService.updateAddress(userDetails.getId(), request),CusAddressResponseDTO.class));
    }

    @PostMapping("/me/cards")
    public ResponseEntity<CardResponseDTO> addCard(@Valid @RequestBody AddCardRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(modelMapper.map(customerService.addCard(userDetails.getId(), request), CardResponseDTO.class));
    }

    @DeleteMapping("/me/cards")
    public ResponseEntity<MessageResponseDTO> deleteCard(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long cardId) {
        customerService.deleteCard(userDetails.getId(), cardId);
        return ResponseEntity.ok(new MessageResponseDTO("Card deleted successfully."));
    }

    @GetMapping("/me/cards")
    public ResponseEntity<List<CardResponseDTO>> getCards(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Card> cards = customerService.getCards(userDetails.getId());
        return ResponseEntity.ok(cards.stream().map(c-> modelMapper.map(c, CardResponseDTO.class)).collect(Collectors.toList()));
    }

    @PutMapping("/me/cards")
    public ResponseEntity<CardResponseDTO> updateCard( @Valid @RequestBody UpdateCardRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(modelMapper.map(customerService.updateCard(userDetails.getId(), request), CardResponseDTO.class));
    }

    @PostMapping("/me/cart")
    public ResponseEntity<CartResponseDTO> addItemToCart(@RequestBody AddCartItemRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Cart cart = customerService.addItemToCart(userDetails.getId(), request);
        return ResponseEntity.ok(modelMapper.map(cart, CartResponseDTO.class));
    }

    @DeleteMapping("/me/cart")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long cartItemId) {
        Cart cart = customerService.removeItemFromCart(userDetails.getId(), cartItemId);
        return ResponseEntity.ok(modelMapper.map(cart, CartResponseDTO.class));
    }

    @DeleteMapping("/me/cart/clear")
    public ResponseEntity<CartResponseDTO> clearCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Cart cart = customerService.clearCart(userDetails.getId());
        return ResponseEntity.ok(modelMapper.map(cart, CartResponseDTO.class));
    }

    @PutMapping("/me/cart")
    public ResponseEntity<CartResponseDTO> updateCartItem(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long cartItemId, @RequestParam int quantity) {
        return ResponseEntity.ok(modelMapper.map(customerService.updateCartItem(userDetails.getId(), cartItemId, quantity), CartResponseDTO.class));
    }

    @PostMapping("/me/order")
    public ResponseEntity<OrderDTO> makeAnOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @Valid @RequestBody MakeAnOrderRequest request
    ) {
        Order order = customerService.makeAnOrder(userDetails.getId(), request);
        return ResponseEntity.ok(modelMapper.map(order, OrderDTO.class));
    }

    @GetMapping("/me/order")
    public ResponseEntity<List<OrderDTO>> getPreviousOrders(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<Order> orders = customerService.getPreviousOrders(userDetails.getId());
        return ResponseEntity.ok(orders.stream().map(o -> modelMapper.map(o, OrderDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("/me/order/active")
    ResponseEntity<List<OrderDTO>> getActiveOrders(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<Order> orders =customerService.getActiveOrders(userDetails.getId());
        return ResponseEntity.ok(orders.stream().map(o -> modelMapper.map(o, OrderDTO.class)).collect(Collectors.toList()));
    }

    @PostMapping("/restaurants/filter")
    public ResponseEntity<List<RestaurantInfoDTO>> filterRestaurants(@RequestBody FilterRestaurantRequest request) {
        List<Restaurant> restaurants = customerService.filterRestaurants(request);
        return ResponseEntity.ok(restaurants.stream().map(r->modelMapper.map(r, RestaurantInfoDTO.class)).collect(Collectors.toList()));
    }

    @PostMapping("/restaurant/{restaurantId}/menu-items/filter")
    public ResponseEntity<List<MenuItemDTO>> filterMenuItems(
            @PathVariable Long restaurantId,
            @RequestBody FilterMenuItemRequest request) {
        List<MenuItem> items = customerService.filterMenuItems(restaurantId, request);
        return ResponseEntity.ok(items.stream().map(i-> modelMapper.map(i,MenuItemDTO.class)).collect(Collectors.toList()));
    }

    @PutMapping("/me/complete-registration")
    public ResponseEntity<CustomerDTO> completeRegistration(@AuthenticationPrincipal CustomUserDetails userDetails){
        Customer customer = customerService.completeRegistration(userDetails.getId());
        return ResponseEntity.ok(modelMapper.map(customer, CustomerDTO.class));
    }

    @PostMapping("/{customerId}/review")
    public ResponseEntity<ReviewDTO> addReview(@PathVariable Long customerId, @RequestBody ReviewRequestDTO dto) {
        dto.setCustomerId(customerId);
        ReviewDTO review = reviewService.addReview(dto);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/me/refund")
    public ResponseEntity<?> createRefund(@RequestBody RefundRequestDTO refundRequestDTO) {
        customerService.createRefund(refundRequestDTO);
        return ResponseEntity.ok("İade talebi oluşturuldu.");
    }

    @PostMapping("/daily-nutrition/add-item")
    public ResponseEntity<Void> addItemToDailyNutrition(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestBody @Valid DailyNutritionItemRequestDTO dto) {
        customerService.addItemToDailyNutrition(userDetails.getId(), dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/daily-nutrition")
    public ResponseEntity<List<DailyNutritionItemDTO>> getDailyNutritionItems(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<DailyNutritionItemDTO> items = customerService.getDailyNutritionItemsByDate(userDetails.getId(), date);
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable Long itemId) throws Throwable {
        customerService.removeItemFromDailyNutrition(userDetails.getId(), date, itemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/daily-nutrition/items/{itemId}")
    public ResponseEntity<Void> updateItemInDailyNutrition(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long itemId,
            @RequestBody @Valid DailyNutritionItemRequestDTO dto) throws Throwable {

        customerService.updateItemInDailyNutrition(userDetails.getId(), itemId, dto);
        return ResponseEntity.noContent().build();
    }

}
