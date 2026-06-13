package com.example.javasepeti.service;

import com.example.javasepeti.dto.*;
import com.example.javasepeti.dto.auth.RegisterDTO;
import com.example.javasepeti.dto.courier.OrderSummaryDTO;
import com.example.javasepeti.dto.courier.RestaurantSummaryDTO;
import com.example.javasepeti.enums.CourierStatus;
import com.example.javasepeti.enums.OrderAssignmentStatus;
import com.example.javasepeti.enums.OrderStatus;
import com.example.javasepeti.model.*;
import com.example.javasepeti.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourierService extends UserService<Courier> {

    BusAddressRepository<Courier, BusAddress<Courier>> courierAddressRepository;
    private final RestaurantRepository restaurantRepository;
    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public CourierService(UserRepository<Courier> userRepository, PasswordEncoder passwordEncoder, BusAddressRepository<Courier, BusAddress<Courier>> courierAddressRepository, RestaurantRepository restaurantRepository, CourierRepository courierRepository, OrderRepository orderRepository, ModelMapper modelMapper) {
        super(userRepository, passwordEncoder, modelMapper);
        this.courierAddressRepository = courierAddressRepository;
        this.restaurantRepository = restaurantRepository;
        this.courierRepository = courierRepository;
        this.orderRepository = orderRepository;
    }


    public Courier createCourierAccount(RegisterDTO registerRequest){
        return add(new Courier(), registerRequest);
    }


    @Transactional
    public Courier updateCourierStatus(Long id, UpdateCourierStatus updateCourierStatus) {
        Courier courier = findById(id);
        courier.setStatus(updateCourierStatus.getStatus());
        return save(courier);
    }


    public Courier updateProfile(Long id, CourierUpdateProfile dto) {
        Courier courier = findById(id);

        if (dto.getName() != null) courier.setName(dto.getName());
        if (dto.getEmail() != null) courier.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) courier.setPhone(dto.getPhoneNumber());
        if (dto.getPassword() != null) {
            String hashedPassword = passwordEncoder.encode(dto.getPassword());
            courier.setPassword(hashedPassword);
        }


        return save(courier);



    }

    public Courier addAddress(Long id, BusAddressRequest request){
        Courier courier = findById(id);
        BusAddress<Courier> address =  courierAddressRepository.findByUser(courier).orElse(null);

        if(address != null){
            throw new RuntimeException("A courier has an only one related address. Try to update it.");
        }

        Type type = new TypeToken<BusAddress<Courier>>() {}.getType();
        address = modelMapper.map(request, type);
        address.setUser(courier);
        address = courierAddressRepository.save(address);
        courier.setAddress(address);
        return userRepository.save(courier);
    }
    public Courier updateAddress(Long id, BusAddressRequest busAddressRequest) {
        Courier courier = findById(id);
        BusAddress<Courier> address =  courierAddressRepository.findByUser(courier).orElseThrow(
                () -> new EntityNotFoundException("There is no address related with courier")
        );
        modelMapper.map(busAddressRequest,address);
        address = courierAddressRepository.save(address);
        address = courierAddressRepository.save(address);
        courier.setAddress(address);
        return userRepository.save(courier);
    }

    public BusAddress<Courier> getAddress(Long id) {
        Courier courier = findById(id);
        return courierAddressRepository.findByUser(courier)
                .orElseThrow(() -> new EntityNotFoundException("No address found for this courier."));
    }

    public List<Restaurant> getNewRestaurants(Long courierId) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));

        BusAddress<Courier> courierAddress = courier.getAddress();
        if (courierAddress == null) {
            throw new RuntimeException("Courier address is not set.");
        } else if (courierAddress.getLocation() == null) {
            throw new RuntimeException("Courier location is not set.");
        } else if (courierAddress.getRangeValue() == null) {
            throw new RuntimeException("Courier range is not set.");
        }

        Location courierLocation = courierAddress.getLocation();
        int courierRange = courierAddress.getRangeValue();

        List<Restaurant> registeredRestaurants = courier.getRestaurants();

        return restaurantRepository.findAll().stream()
                .filter(restaurant -> !registeredRestaurants.contains(restaurant))
                .filter(restaurant -> {
                    BusAddress<Restaurant> restaurantAddress = restaurant.getBusAddress();
                    if (restaurantAddress == null || restaurantAddress.getLocation() == null) {
                        return false;
                    }
                    double distance = courierLocation.calcDistance(restaurantAddress.getLocation());
                    return distance <= courierRange;
                })
                .collect(Collectors.toList());
    }


    public List<Restaurant> getRegisteredRestaurants(Long courierId) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));

        return courier.getRestaurants();
    }


    public void addRestaurantToCourier(Long courierId, Long restaurantId) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found."));

        boolean alreadyExists = restaurant.getCouriers().stream()
                .anyMatch(c -> c.getUserId().equals(courierId));

        if (!alreadyExists) {
            restaurant.getCouriers().add(courier);
            courier.getRestaurants().add(restaurant);
            restaurantRepository.save(restaurant);
            courierRepository.save(courier);
        } else {
            throw new RuntimeException("Restaurant already exists in courier");
        }
    }

    public List<Order> getActiveOrders(Long courierId) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier bulunamadı."));

        return courier.getOrders().stream()
                .filter(order -> order.getStatus() != OrderStatus.DELIVERED)
                .collect(Collectors.toList());
    }


    public List<Order> getPreviousOrders(Long courierId, LocalDate date) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier bulunamadı."));

        return courier.getOrders().stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .filter(order -> order.getDate().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }


    public Order getOrder(Long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order bulunamadı."));
    }

    public void updateOrderAssignmentStatus(Long orderId, Long courierId, OrderAssignmentStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order bulunamadı."));

        Courier courier = order.getCourier();
        if (courier == null || !courier.getUserId().equals(courierId)) {
            throw new RuntimeException("Bu order bu courier'a ait değil.");
        }

        if (newStatus == OrderAssignmentStatus.ACCEPTED) {
            order.setAssignmentStatus(OrderAssignmentStatus.ACCEPTED);
            courier.setStatus(CourierStatus.BUSY);
        }
        else if (newStatus == OrderAssignmentStatus.REJECTED) {
            order.setAssignmentStatus(OrderAssignmentStatus.REJECTED);
            courier.getOrders().remove(order);
            order.setCourier(null);
        }
        else {
            throw new RuntimeException("Geçersiz durum güncellemesi.");
        }

        orderRepository.save(order);
    }

    public Order getCurrentPacket(Long courierId) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));

        if (courier.getStatus() != CourierStatus.BUSY) return null;

        return courier.getOrders().stream()
                .filter(order -> order.getAssignmentStatus() == OrderAssignmentStatus.ACCEPTED)
                .filter(order -> order.getStatus() != OrderStatus.DELIVERED)
                .findFirst()
                .orElse(null);
    }


    public void updateOrderStatus(Long courierId, OrderStatus newStatus) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));

        Order order = courier.getOrders().stream()
                .filter(o -> o.getAssignmentStatus() == OrderAssignmentStatus.ACCEPTED)
                .filter(o -> o.getStatus() != OrderStatus.DELIVERED)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No valid order found to update."));

        if (newStatus == OrderStatus.ON_DELIVERY && order.getStatus() == OrderStatus.PREPARATION) {
            order.setStatus(OrderStatus.ON_DELIVERY);
        } else if (newStatus == OrderStatus.DELIVERED && order.getStatus() == OrderStatus.ON_DELIVERY) {
            order.setStatus(OrderStatus.DELIVERED);
            courier.setStatus(CourierStatus.AVAILABLE);
            courierRepository.save(courier);
        } else {
            throw new RuntimeException("Invalid status transition.");
        }
        orderRepository.save(order);
    }


    public List<Courier> getByStatus(CourierStatus status) {
        return courierRepository.findByStatus(status);
    }
}