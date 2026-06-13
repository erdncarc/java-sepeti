package com.example.javasepeti.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.example.javasepeti.dto.*;
import com.example.javasepeti.dto.auth.RegisterDTO;
import com.example.javasepeti.dto.customer.*;
import com.example.javasepeti.dto.general.ReviewDTO;
import com.example.javasepeti.enums.AccountStatus;
import com.example.javasepeti.enums.Label;
import com.example.javasepeti.enums.OrderStatus;
import com.example.javasepeti.enums.SortDirection;
import com.example.javasepeti.model.*;
import com.example.javasepeti.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.javasepeti.exception.EmailAlreadyExistsException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService extends UserService<Customer> {

    protected final CardService cardService;
    protected final CusAddressService cusAddressService;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final ReviewRepository reviewRepository;
    private final CartService cartService;

    private final OrderService orderService;
    private final ItemRepository itemRepository;
    private final DailyNutritionRepository dailyNutritionRepository;
    private final RefundRepository refundRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
                           CardService cardService, CusAddressService cusAddressService, RestaurantRepository restaurantRepository,
                           CartService cartService, OrderService orderService, ModelMapper modelMapper, OrderRepository orderRepository,
                           MenuItemRepository menuItemRepository, ReviewRepository reviewRepository, ItemRepository itemRepository,
                           DailyNutritionRepository dailyNutritionRepository, RefundRepository refundRepository){
        super(customerRepository, passwordEncoder,modelMapper);
        this.cardService = cardService;
        this.cusAddressService = cusAddressService;
        this.restaurantRepository = restaurantRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.orderService = orderService;
        this.menuItemRepository = menuItemRepository;
        this.reviewRepository = reviewRepository;
        this.itemRepository = itemRepository;
        this.dailyNutritionRepository = dailyNutritionRepository;
        this.refundRepository = refundRepository;
    }

    public void createCustomerAccount(RegisterDTO registerRequest) {
        Customer customer = new Customer();
        super.add(customer, registerRequest);
    }

    public Customer updateProfileInfo(Long id, CustomerUpdateInfoRequest customerUpdateInfoRequest) {

        Customer customer = this.findById(id);

        if (this.existsByEmail(customerUpdateInfoRequest.getEmail()) && !customer.getEmail().equals(customerUpdateInfoRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use.");
        }
        modelMapper.map(customerUpdateInfoRequest,customer);

        return this.save(customer);
    }

    public Card addCard(Long customerId, AddCardRequest addCardRequest){
        return cardService.add(findById(customerId), addCardRequest);
    }

    public void deleteCard(Long customerId, Long cardId){
        cardService.delete(findById(customerId), cardId);
    };


    public CusAddress addAddressToCustomer(Long id, AddCusAddressRequest addCusAddressRequest){
        Customer customer = this.findById(id);
        return cusAddressService.add(customer, addCusAddressRequest);

    }

    public List<Card> getCards(Long id){
        return findById(id).getCards();
    }

    public void deleteAddressFromCustomer(Long customerId, Long addressId) {
        CusAddress cusAddress= cusAddressService.findByAddressIdAndUser(addressId,findById(customerId));
        cusAddressService.addressRepository.delete(cusAddress);
    }

    public List<CusAddress> getAddresses(Long id) {
        return findById(id).getAddresses();
    }

    public CusAddress updateAddress(Long customerId,UpdateCusAddressRequest updateCusAddressRequest) {
        return cusAddressService.update(findById(customerId),updateCusAddressRequest);
    }

    public Card updateCard(Long customerId, UpdateCardRequest request) {
        return cardService.update(findById(customerId), request);
    }

    public void deleteAccount(Long id) {
        Customer customer = this.findById(id);
        userRepository.deleteById(id);
    }

    public List<Restaurant> getAllRestaurants(Long addressId, Long userId) {
        CusAddress cusAddress =  cusAddressService.findByAddressIdAndUser(addressId, this.findById(userId));
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .filter(r -> {
                    BusAddress<?> busAddress = r.getBusAddress();
                    return busAddress != null && busAddress.getLocation().getLatitude() != null && busAddress.getLocation().getLongitude() != null && busAddress.getRangeValue() != null &&
                            busAddress.getLocation().calcDistance(cusAddress.getLocation()) <= busAddress.getRangeValue();
                }).collect(Collectors.toList());

    }

    public Cart addItemToCart(Long id, AddCartItemRequest request) {
        Customer customer = findById(id);
        Cart cart = customer.getActiveCart();

        if (cart == null) {
            cart = cartService.createCart(customer);
        }
        return cartService.addItemToCart(cart, request);
    }


    public Cart removeItemFromCart(Long id, Long itemId){
        Customer customer = findById(id);
        Cart cart = customer.getActiveCart();

        if (cart == null) {
            cart = cartService.createCart(customer);
        }
        return cartService.removeItemFromCart(cart,itemId);

    }


    public Cart clearCart(Long customerId){
        Customer customer = findById(customerId);
        return cartService.clearCart(customer.getActiveCart());
    }

    public Cart updateCartItem(Long id, Long cartItemId, int quantity) {
        Customer customer = findById(id);
        Cart cart = customer.getActiveCart();
        if (cart == null) {
            throw new IllegalStateException("Customer has no active cart.");
        }
        return cartService.updateCartItem(cart, cartItemId, quantity);
    }



    public Order makeAnOrder(Long id, MakeAnOrderRequest request){
         return orderService.addOrder(findById(id), request);
    }

    public List<Order> getPreviousOrders(Long id) {
        return orderService.getCustomerOrders(findById(id));
    }

    public List<Order> getActiveOrders(Long customerId) {
        Customer customer = findById(customerId);
        return orderService.getCustomerOrders(customer).stream()
                .filter(order ->
                        order.getStatus() != OrderStatus.DELIVERED &&
                                order.getStatus() != OrderStatus.CANCELLED
                )
                .collect(Collectors.toList());
    }

    public Cart getCart(Long id) {
        Customer customer = findById(id);
        Cart cart = customer.getActiveCart();

        if (cart == null) {
            cart = cartService.createCart(customer);
        }
        return cartService.getCart(cart.getCartId());
    }

    public List<Restaurant> filterRestaurants(FilterRestaurantRequest request) {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .filter(r -> {
                    BusAddress<?> busAddress = r.getBusAddress();
                    if (busAddress == null || busAddress.getLocation().getLatitude() == null || busAddress.getLocation().getLongitude() == null || busAddress.getRangeValue() == null) {
                        return false; // Skip if location data is incomplete
                    }

                    // Calculate distance
                    double distance = request.getLocation().calcDistance(r.getBusAddress().getLocation());

                    // Skip if out of delivery range
                    if (busAddress.getRangeValue() != null && distance > busAddress.getRangeValue()) {
                        return false;
                    }

                    // Check open/closed filtering
                    boolean isOpen = r.isOpen();
                    if (request.isOpen() && !isOpen) return false;
                    if (request.isClosed() && isOpen) return false;

                    return true;
                })
                .sorted((a, b) -> {
                    Comparator<Restaurant> comparator;

                    switch (request.getSortBy()) {
                        case DISTANCE:
                            comparator = Comparator.comparingDouble(r -> r.getBusAddress().getLocation().calcDistance(request.getLocation()));
                            break;
                        case RATE:
                        default:
                            comparator = Comparator.comparingDouble(r ->
                                    r.getRating() != null ? r.getRating().total().doubleValue() : 0.0
                            );
                            break;
                    }

                    if (request.getSortDirection() == SortDirection.DESC) {
                        comparator = comparator.reversed();
                    }

                    return comparator.compare(a, b);
                })
                .collect(Collectors.toList());
    }

    public List<MenuItem> filterMenuItems(Long restaurantId, FilterMenuItemRequest req) {
        List<MenuItem> items = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"))
                .getMenuItems();

        items = items.stream()
                .filter(i -> i.getCalorie() >= req.getMinCalories() && i.getCalorie() <= req.getMaxCalories())
                .filter(i -> i.getProtein() >= req.getMinProtein() && i.getProtein() <= req.getMaxProtein())
                .filter(i -> i.getFat() >= req.getMinFats() && i.getFat() <= req.getMaxFats())
                .filter(i -> i.getCarb() >= req.getMinCarbs() && i.getCarb() <= req.getMaxCarbs())
                .filter(i -> !req.isVegan() || i.getLabels().contains(Label.VEGAN))
                .filter(i -> !req.isVegetarian() || i.getLabels().contains(Label.VEGETARIAN))
                .filter(i -> req.getAllergens() == null ||
                        req.getAllergens().stream().noneMatch(a -> i.getAllergens().contains(a))) // allergens specified by the user are excluded
                .collect(Collectors.toList());

        Comparator<MenuItem> comparator;

        switch (req.getSortBy()) {
            case RATE:
                comparator = Comparator.comparing(i -> i.getRating() != null ? i.getRating() : BigDecimal.ZERO);
                break;
            case CALORIES:
                comparator = Comparator.comparingInt(MenuItem::getCalorie);
                break;
            case PROTEIN:
                comparator = Comparator.comparingInt(MenuItem::getProtein);
                break;
            case FATS:
                comparator = Comparator.comparingInt(MenuItem::getFat);
                break;
            case CARBS:
                comparator = Comparator.comparingInt(MenuItem::getCarb);
                break;
            case PRICE:
            default:
                comparator = Comparator.comparing(MenuItem::getPrice);
                break;
        }


        if (req.getSortDirection() == SortDirection.DESC) {
            comparator = comparator.reversed();
        }

        items.sort(comparator);
        return items;
    }


    public Customer completeRegistration(Long customerId){
        Customer customer = findById(customerId);
        customer.setAccountStatus(AccountStatus.AVAILABLE);
        return save(customer);
    }

    public void createRefund(RefundRequestDTO dto) {
        Order order = orderService.getOrder(dto.getOrderId());

        if (order.getRefund() != null) {
            throw new RuntimeException("Bu sipariş için zaten iade talebi oluşturulmuş.");
        }

        Refund refund = new Refund();
        refund.setReason(dto.getReason());
        refund.setOrder(order);

        order.setRefund(refund);

        refundRepository.save(refund);
    }

    public void addItemToDailyNutrition(Long id, DailyNutritionItemRequestDTO dto) {
        Customer customer = findById(id);
        LocalDate date = dto.getDate();

        DailyNutrition dailyNutrition = dailyNutritionRepository
                .findByCustomerUserIdAndDate(id, date)
                .orElseGet(() -> {
                    DailyNutrition dn = new DailyNutrition();
                    dn.setCustomer(customer);
                    dn.setDate(date);
                    return dailyNutritionRepository.save(dn);
                });

        Item item = new Item();
        item.setName(dto.getName());
        item.setProtein(dto.getProtein());
        item.setCarb(dto.getCarb());
        item.setFat(dto.getFat());
        item.setCalorie(dto.getCalorie());
        item.setQuantity(dto.getQuantity());

        item = (Item) itemRepository.save(item);
        dailyNutrition.getItems().add(item);
        dailyNutritionRepository.save(dailyNutrition);
    }

    public List<DailyNutritionItemDTO> getDailyNutritionItemsByDate(Long userId, LocalDate date) {
        Optional<DailyNutrition> optionalNutrition = dailyNutritionRepository.findByCustomerUserIdAndDate(userId, date);

        if (optionalNutrition.isEmpty()) {
            return Collections.emptyList();
        }

        DailyNutrition nutrition = optionalNutrition.get();

        return nutrition.getItems().stream()
                .map(item -> new DailyNutritionItemDTO(
                        item.getItemId(),
                        item.getName(),
                        item.getImage(),
                        item.getQuantity(),
                        item.getCalorie(),
                        item.getProtein(),
                        item.getCarb(),
                        item.getFat()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeItemFromDailyNutrition(Long userId, LocalDate date, Long itemId) throws Throwable {
        DailyNutrition dailyNutrition = dailyNutritionRepository.findByCustomerUserIdAndDate(userId, date)
                .orElseThrow(() -> new RuntimeException("DailyNutrition not found for this user and date"));

        Item item = (Item) itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        boolean removed = dailyNutrition.getItems().remove(item);

        if (!removed) {
            throw new RuntimeException("Item does not exist in DailyNutrition");
        }

        dailyNutritionRepository.save(dailyNutrition);

        boolean isUsedElsewhere = dailyNutritionRepository.existsByItemsContaining(item);
        if (!isUsedElsewhere) {
            itemRepository.delete(item);
        }
    }

    @Transactional
    public void updateItemInDailyNutrition(Long userId, Long itemId, DailyNutritionItemRequestDTO dto) throws Throwable {
        DailyNutrition dailyNutrition = dailyNutritionRepository
                .findByCustomerUserIdAndDate(userId, dto.getDate())
                .orElseThrow(() -> new RuntimeException("DailyNutrition not found for this user and date"));

        Item item = (Item) itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!dailyNutrition.getItems().contains(item)) {
            throw new RuntimeException("Item is not part of this DailyNutrition");
        }

        item.setName(dto.getName());
        item.setProtein(dto.getProtein());
        item.setCarb(dto.getCarb());
        item.setFat(dto.getFat());
        item.setCalorie(dto.getCalorie());

        if (dto.getQuantity() != null && dto.getQuantity() >= 0) {
            item.setQuantity(dto.getQuantity());
        } else {
            throw new IllegalArgumentException("Quantity must be zero or positive");
        }

        itemRepository.save(item);
    }


}