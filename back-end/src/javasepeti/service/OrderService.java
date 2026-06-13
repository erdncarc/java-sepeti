package com.example.javasepeti.service;

import com.example.javasepeti.dto.MakeAnOrderRequest;
import com.example.javasepeti.enums.CartStatus;
import com.example.javasepeti.enums.PaymentMethod;
import com.example.javasepeti.model.*;
import com.example.javasepeti.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    OrderRepository orderRepository;
    CardService cardService;

    CusAddressService cusAddressService;
    CartRepository cartRepository;

    MenuItemRepository menuItemRepository;

    ModelMapper modelMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, CardService cardService, CusAddressService cusAddressService,
                        CartRepository cartRepository, MenuItemRepository menuItemRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.cardService = cardService;
        this.cusAddressService = cusAddressService;
        this.cartRepository = cartRepository;
        this.menuItemRepository = menuItemRepository;
        this.modelMapper = modelMapper;
    }


    public Order addOrder(Customer customer, MakeAnOrderRequest request){
        // Check if cart is empty
        if (customer.getActiveCart() == null || customer.getActiveCart().getCartItems() == null || customer.getActiveCart().getCartItems().isEmpty()) {
            throw new IllegalStateException("Your cart is empty. Please add items before placing an order.");
        }

        // Check if address exists
        CusAddress address = cusAddressService.findByAddressIdAndUser(request.getAddressId(), customer);
        if (address == null) {
            throw new IllegalArgumentException("Selected address is invalid or not found.");
        }

        Card card = null;
        Restaurant restaurant = customer.getActiveCart().getRestaurant();

        // Check if restaurant is open
        if (!restaurant.isOpen()) {
            throw new IllegalStateException("This restaurant is currently closed.");
        }

        // Check delivery range
        BusAddress<?> busAddress = restaurant.getBusAddress();
        if (busAddress == null || busAddress.getLocation().getLatitude() == null || busAddress.getLocation().getLongitude() == null || busAddress.getRangeValue() == null) {
            //throw new IllegalStateException("Restaurant location or delivery range is not properly set.");
        }

        /*
        double distance = GeoUtils.calculateDistance(
                address.getLatitude(), address.getLongitude(),
                busAddress.getLatitude(), busAddress.getLongitude()
        );


        if (distance > busAddress.getRangeValue()) {
            throw new IllegalStateException(String.format(
                    "This restaurant does not deliver to your address. Distance: %.2f km, Delivery range: %.2f km.",
                    distance, busAddress.getRangeValue().doubleValue()
            ));
        }
        */



        if (request.getPaymentMethod() == PaymentMethod.CARD) {
            if (request.getCardId() == null) {
                throw new IllegalArgumentException("Card ID must be provided for card payment.");
            }
            card = cardService.findByIdAndCustomer(request.getCardId(), customer);
        }

        Order order = new Order(customer, card, address, request.getPaymentMethod(), request.getNote());

        for(CartItem cartItem : order.getCart().getCartItems()){
            menuItemRepository.findByHashIdAndParentIdNotNull(cartItem.getMenuItem().getHashId()).ifPresentOrElse(cartItem::setMenuItem, () ->{
                MenuItem nmi = new MenuItem();
                modelMapper.map(cartItem.getMenuItem(), nmi);
                nmi.setItemId(null);
                nmi.setParentId(cartItem.getMenuItem().getItemId());
                nmi = menuItemRepository.save(nmi);
                cartItem.setMenuItem(nmi);
            });

        }

        customer.getOrders().add(order);
        order.getRestaurant().getOrders().add(order);


        customer.getActiveCart().setRestaurant(order.getRestaurant());
        customer.getActiveCart().setCartStatus(CartStatus.ORDERED);
        customer.setActiveCart(null);

        Order new_order =  orderRepository.save(order);
        //cartRepository.save(customer.getCart());
        return new_order;
    }


    public List<Order> getCustomerOrders(Customer customer) {
        return orderRepository.findByCustomer(customer);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı: " + orderId));
    }
}
