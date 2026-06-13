package com.example.javasepeti.service;

import com.example.javasepeti.dto.customer.AddCartItemRequest;
import com.example.javasepeti.model.*;
import com.example.javasepeti.repository.CartItemRepository;
import com.example.javasepeti.repository.CartRepository;
import com.example.javasepeti.repository.MenuItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class CartService {

    protected final CartRepository cartRepository;
    protected final CartItemRepository cartItemRepository;
    protected final MenuItemRepository menuItemRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, MenuItemRepository menuItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public Cart createCart(Customer customer) {
        Cart cart = new Cart();
        cart.setCustomer(customer);
        customer.setActiveCart(cart);
        return cartRepository.save(cart);
    }

    public Cart addItemToCart(Cart cart, AddCartItemRequest request) {

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }

        CartItem cartItem = new CartItem();

        if (cart.getRestaurant() == null) {
            MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId()).orElseThrow(
                    () -> new EntityNotFoundException(String.format("There is no item with id %d", request.getMenuItemId()))
            );
            cartItem.setMenuItem(menuItem);
            cart.setRestaurant(menuItem.getRestaurant());
        } else {
            cartItem.setMenuItem(menuItemRepository.findByItemIdAndRestaurant(
                    request.getMenuItemId(), cart.getRestaurant()).orElseThrow(
                    () -> new EntityNotFoundException(String.format("There is no item with id %d", request.getMenuItemId()))
            ));

            CartItem conflictItem = cart.getCartItems().stream()
                    .filter(_cartItem -> Objects.equals(
                            _cartItem.getMenuItem().getItemId(),
                            cartItem.getMenuItem().getItemId()))
                    .findFirst()
                    .orElse(null);

            if (conflictItem != null) {
                conflictItem.setQuantity(conflictItem.getQuantity() + request.getQuantity());
                cartItemRepository.save(conflictItem);
                return cartRepository.save(cart);
            }
        }

        cartItem.setQuantity(request.getQuantity());
        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);

        cartItemRepository.save(cartItem);
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(Cart cart, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id " + cartItemId));

        // Remove the item from the cart's list
        cart.getCartItems().removeIf(item -> item.getCartItemId().equals(cartItemId));

        // Disconnect the CartItem from the cart
        cartItem.setCart(null);
        cartItemRepository.delete(cartItem);

        // If cart is now empty, remove the restaurant info
        if (cart.getCartItems().isEmpty()) {
            cart.setRestaurant(null);
        }

        return cartRepository.save(cart);
    }

    public Cart getCart(Long cartId) {

        return cartRepository.findById(cartId).orElseThrow(
                () -> new EntityNotFoundException(String.format("There is no cart with id: %d", cartId))
        );
    }

    public Cart clearCart(Cart cart) {
        // Make a copy to avoid ConcurrentModificationException
        for (CartItem item : new ArrayList<>(cart.getCartItems())) {
            removeItemFromCart(cart, item.getCartItemId());
        }

        // Redundant, but safe fallback
        cart.setRestaurant(null);
        return cartRepository.save(cart);
    }

    public Cart updateCartItem(Cart cart, Long cartItemId, int quantity) {
        CartItem cartItem =  cartItemRepository.findByCartItemIdAndCart(cartItemId,cart).orElseThrow(
                () -> new EntityNotFoundException("There is no item in cart")
        );

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        cartItem.setQuantity(quantity);
        //return cartRepository.save(cart);
        cartItemRepository.save(cartItem); // Save the item directly
        return cart;

    }

}
