package com.example.javasepeti.service;

import com.example.javasepeti.dto.customer.ReviewRequestDTO;
import com.example.javasepeti.dto.general.ReviewDTO;
import com.example.javasepeti.enums.OrderStatus;
import com.example.javasepeti.model.*;
import com.example.javasepeti.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReviewService {

    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    ModelMapper modelMapper;

    @Autowired
    public ReviewService(CustomerRepository customerRepository, RestaurantRepository restaurantRepository,
                         ModelMapper modelMapper, OrderRepository orderRepository, MenuItemRepository menuItemRepository,
                         ReviewRepository reviewRepository){
        this.restaurantRepository = restaurantRepository;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ReviewDTO addReview(ReviewRequestDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));;

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        MenuItem menuItem = menuItemRepository.findById(dto.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException("You can only review delivered orders.");
        }

        if (!order.getCustomer().getUserId().equals(dto.getCustomerId())) {
            throw new RuntimeException("You are not authorized to review this order.");
        }

        Rating rating = new Rating();
        rating.setTaste(dto.getTaste());
        rating.setDelivery(dto.getDelivery());

        Review review = new Review();
        review.setRating(rating);
        review.setComment(dto.getComment());
        review.setDate(new Date());
        review.setCustomer(customer);
        review.setRestaurant(restaurant);
        review.setOrder(order);
        review.setMenuItem(menuItem);

        reviewRepository.save(review);

        // Add to customer
        customer.getReviews().add(review);

        // Add to restaurant
        restaurant.getReviews().add(review);

        // Update ratings
        updateMenuItemRating(menuItem);
        updateRestaurantRating(menuItem.getRestaurant());

        return modelMapper.map(review, ReviewDTO.class);
    }

    private void updateMenuItemRating(MenuItem menuItem) {
        if (menuItem == null) {
            throw new IllegalArgumentException("MenuItem cannot be null");
        }

        List<Review> reviews = reviewRepository.findByMenuItem(menuItem);

        if (reviews.isEmpty()) return;

        double avg = reviews.stream()
                .map(r -> r.getRating().total().doubleValue())
                .mapToDouble(Double::doubleValue)
                .average().orElse(0.0);

        BigDecimal rounded = BigDecimal.valueOf(Math.round(avg * 10) / 10.0);
        menuItem.setRating(rounded);
        menuItemRepository.save(menuItem);
    }

    private void updateRestaurantRating(Restaurant restaurant) {
        List<Review> reviews = restaurant.getReviews();
        if (reviews.isEmpty()) return;

        BigDecimal totalTaste = BigDecimal.ZERO;
        BigDecimal totalDelivery = BigDecimal.ZERO;

        for (Review r : reviews) {
            totalTaste = totalTaste.add(r.getRating().getTaste());
            totalDelivery = totalDelivery.add(r.getRating().getDelivery());
        }

        int count = reviews.size();
        Rating avg = new Rating();

        double tasteAvg = totalTaste.doubleValue() / count;
        double deliveryAvg = totalDelivery.doubleValue() / count;

        double roundedTaste = Math.round(tasteAvg * 10.0) / 10.0;
        double roundedDelivery = Math.round(deliveryAvg * 10.0) / 10.0;

        avg.setTaste(BigDecimal.valueOf(roundedTaste));
        avg.setDelivery(BigDecimal.valueOf(roundedDelivery));

        restaurant.setRating(avg);
        restaurantRepository.save(restaurant);
    }

    public void replyToReview(Long reviewId, Long restaurantId, String reply) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Check that the restaurant owns the reviewed item
        if (!review.getMenuItem().getRestaurant().getUserId().equals(restaurantId)) {
            throw new RuntimeException("You are not allowed to reply to this review");
        }

        // Prevent double-reply
        if (review.getReply() != null) {
            throw new RuntimeException("This review has already been replied");
        }

        review.setReply(reply);
        review.setReplyDate(new Date());
        reviewRepository.save(review);
    }

    public List<ReviewDTO> getReviewsForCourier(Long courierId) {
        List<Review> reviews = reviewRepository.findByOrder_Courier_UserId(courierId);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsForRestaurant(Long restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurant_UserId(restaurantId);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());
    }


}
