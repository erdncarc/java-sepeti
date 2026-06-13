package com.example.javasepeti.repository;

import com.example.javasepeti.model.Cart;
import com.example.javasepeti.model.CartItem;
import com.example.javasepeti.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartItemIdAndCart(Long cartItemId, Cart cart);

    List<CartItem> findByMenuItem(MenuItem menuItem);

}
