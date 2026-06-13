package com.example.javasepeti.service;

import com.example.javasepeti.enums.CourierStatus;
import com.example.javasepeti.enums.OrderStatus;
import com.example.javasepeti.model.BusAddress;
import com.example.javasepeti.model.Courier;
import com.example.javasepeti.model.Order;
import com.example.javasepeti.model.Restaurant;
import com.example.javasepeti.repository.CourierRepository;
import com.example.javasepeti.repository.OrderRepository;
import com.example.javasepeti.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantOrderService {

    private final OrderRepository orderRepository;
    private final CourierService courierService;
    private final CourierRepository courierRepository;
    private final RestaurantService restaurantService;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantOrderService(OrderRepository orderRepository, CourierService courierService,
                                  CourierRepository courierRepository, RestaurantService restaurantService,
                                  RestaurantRepository restaurantRepository) {
        this.orderRepository = orderRepository;
        this.courierService = courierService;
        this.courierRepository = courierRepository;
        this.restaurantService = restaurantService;
        this.restaurantRepository = restaurantRepository;
    }

    public Order acceptOrder(Long orderId) {
        Order order = getOrderOrThrow(orderId);
        order.setStatus(OrderStatus.PREPARATION);
        return orderRepository.save(order);
    }

    public Order cancelOrder(Long orderId) {
        Order order = getOrderOrThrow(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = getOrderOrThrow(orderId);
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    private Order getOrderOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }


    public List<Courier> getAvailableCouriersForAssignment(Long resId) {
        Restaurant restaurant = restaurantRepository.findById(resId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + resId));

        BusAddress<Restaurant> resAddress = restaurant.getBusAddress();

        if (resAddress == null || resAddress.getLocation().getLatitude() == null ||
                resAddress.getLocation().getLongitude() == null || resAddress.getRangeValue() == null) {
            throw new IllegalStateException("Restaurant location or delivery range is not properly set.");
        }

        double restaurantRange = resAddress.getRangeValue();

        List<Courier> availableCouriers = courierService.getByStatus(CourierStatus.AVAILABLE);

        return availableCouriers.stream()
                .filter(courier -> {
                    BusAddress<?> courierAddress = courier.getAddress();
                    if (courierAddress == null ||
                            courierAddress.getLocation().getLatitude() == null || courierAddress.getLocation().getLongitude() == null ||
                            courierAddress.getRangeValue() == null) {
                        return false;
                    }


                    double courierRange = courierAddress.getRangeValue();

                    double distance = resAddress.getLocation().calcDistance(courierAddress.getLocation());

                    return distance <= restaurantRange && distance <= courierRange;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void assignCourierToOrder(Long orderId, Long courierId) {
        Order order = getOrderOrThrow(orderId);
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found with id: " + courierId));

        if (courier.getStatus() != CourierStatus.AVAILABLE) {
            throw new IllegalStateException("Courier is not available.");
        }

        if (order.getCourier() != null) {
            Courier previousCourier = order.getCourier();
            previousCourier.getOrders().remove(order);
            courierRepository.save(previousCourier);
        }

        order.setCourier(courier);
        courier.getOrders().add(order);

        courierRepository.save(courier);
        orderRepository.save(order);
    }

    public void updateOrderStatus(Long resId, Long orderId, OrderStatus newStatus) {
        Order order = getOrderOrThrow(orderId);

        if (!order.getRestaurant().getUserId().equals(resId)) {
            throw new RuntimeException("Order does not belong to the given restaurant.");
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
    }
}
