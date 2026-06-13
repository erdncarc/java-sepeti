package com.example.javasepeti.service;

import com.example.javasepeti.dto.*;
import com.example.javasepeti.dto.auth.RegisterDTO;
import com.example.javasepeti.dto.restaurant.UpdateRestaurantInfoDTO;
import com.example.javasepeti.enums.AccountStatus;
import com.example.javasepeti.model.*;
import com.example.javasepeti.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class RestaurantService extends UserService<Restaurant> {

    MenuItemRepository menuItemRepository;

    BusAddressRepository<Restaurant, BusAddress<Restaurant>> resAddressRepository;

    OpeningHourRepository openingHourRepository;


    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, PasswordEncoder passwordEncoder, MenuItemRepository menuItemRepository,
                             BusAddressRepository<Restaurant, BusAddress<Restaurant>> resAddressRepository, OpeningHourRepository openingHourRepository,
                             ModelMapper modelMapper) {
        super(restaurantRepository, passwordEncoder, modelMapper);
        this.menuItemRepository = menuItemRepository;
        this.resAddressRepository = resAddressRepository;
        this.openingHourRepository = openingHourRepository;
    }



    public List<Restaurant> getAll(){
        return this.userRepository.findAll();
    }

    public Restaurant createRestaurantAccount(RegisterDTO registerRequest){
        Restaurant restaurant = new Restaurant();
        return add(restaurant, registerRequest);
    }

    public Restaurant updateRestaurantInfo(Long id, UpdateRestaurantInfoDTO updateRestaurantInfo) {
        Restaurant restaurant = findById(id);
        modelMapper.map(updateRestaurantInfo, restaurant);
        return save(restaurant);
    }

    public void deleteAccount(Long id) {
        Restaurant restaurant = findById(id);
        userRepository.delete(restaurant);
    }


    public MenuItem addMenuItem(Long restaurantId, AddMenuItemRequest request) {
        Restaurant restaurant = findById(restaurantId);
        MenuItem menuItem = modelMapper.map(request, MenuItem.class);
        menuItem.setQuantity(request.getQuantity()); // Manually map inherited field
        menuItem.setRestaurant(restaurant);
        menuItem.setHashId(menuItem.createHash());
        return menuItemRepository.save(menuItem);
    }

    public void deleteMenuItem(Long restaurantId, Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));
        if (!menuItem.getRestaurant().getUserId().equals(restaurantId)) {
            throw new RuntimeException("The menu item does not belong to this restaurant.");
        }
        menuItem.setRestaurant(null);
        menuItemRepository.delete(menuItem);
    }

    public MenuItem updateMenuItem(Long restaurantId,UpdateMenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));
        if (!menuItem.getRestaurant().getUserId().equals(restaurantId)) {
            throw new RuntimeException("The menu item does not belong to this restaurant.");
        }
        modelMapper.map(request,menuItem);
        menuItem.setHashId(menuItem.createHash());
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getMenuItems(Long restaurantId) {
        return menuItemRepository.findByRestaurantUserId(restaurantId);
    }

    public MenuItem getMenuItem(Long restaurantId, Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));
        if (!menuItem.getRestaurant().getUserId().equals(restaurantId)) {
            throw new RuntimeException("The menu item does not belong to this restaurant.");
        }
        return menuItem;
    }


    public Restaurant addAddress(Long id, BusAddressRequest request){
        Restaurant restaurant = findById(id);
        BusAddress<Restaurant> address =  resAddressRepository.findByUser(restaurant).orElse(null);
        if(address != null){
            throw new RuntimeException("A restaurant has an only one related address. Try to update it.");
        }
        Type type = new TypeToken<BusAddress<Restaurant>>() {}.getType();
        address = modelMapper.map(request, type);
        address.setUser(restaurant);
        address = resAddressRepository.save(address);
        restaurant.setBusAddress(address);
        return userRepository.save(restaurant);
    }

    public Restaurant updateAddress(Long id, BusAddressRequest busAddressRequest) {

        Restaurant restaurant = findById(id);
        BusAddress<Restaurant> address =  resAddressRepository.findByUser(restaurant).orElseThrow(
                () -> new EntityNotFoundException("There is no address related with restaurant")
        );

        modelMapper.map(busAddressRequest,address);
        address = resAddressRepository.save(address);
        restaurant.setBusAddress(address);
        return userRepository.save(restaurant);
    }


    public BusAddress<Restaurant> getAddress(Long id) {
        Restaurant restaurant = findById(id);
        return resAddressRepository.findByUser(restaurant)
                .orElseThrow(() -> new EntityNotFoundException("No address found for this restaurant"));
    }


    public Restaurant setOpeningHours(Long restaurantId, List<OpeningHourRequest> requests) {
        Restaurant restaurant = findById(restaurantId);

        // Remove old entries
        openingHourRepository.deleteAll(restaurant.getOpeningHours());
        restaurant.getOpeningHours().clear();

        // Add new
        for (OpeningHourRequest req : requests) {
            OpeningHour hour = new OpeningHour();
            hour.setDayOfWeek(req.getDayOfWeek());
            hour.setOpenTime(req.getOpenTime());
            hour.setCloseTime(req.getCloseTime());

            hour.setRestaurant(restaurant);
            restaurant.getOpeningHours().add(hour);
        }

        return userRepository.save(restaurant);
    }


    public List<OpeningHour> getOpeningHours(Long restaurantId) {
        Restaurant restaurant = findById(restaurantId);
        return restaurant.getOpeningHours();
    }

    public Restaurant completeRegistration(Long restaurantId){
        Restaurant restaurant = findById(restaurantId);
        if(restaurant.getBusAddress() == null || restaurant.getPhone() != null || restaurant.getOpeningHours().isEmpty()){
            throw new RuntimeException("Registration not compleate");
        }

        restaurant.setAccountStatus(AccountStatus.AVAILABLE);
        return save(restaurant);
    }

}