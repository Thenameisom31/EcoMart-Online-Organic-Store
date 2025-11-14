package com.example.demo.service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;

import java.util.List;

public interface CartService {
    List<CartItem> getCartItems(User user);
    void addToCart(User user, Product product, int quantity);
    void removeFromCart(User user, Product product);
    void clearCart(User user);
    void updateQuantity(User user, Long productId, int quantity);
    int getCartCount(User user);
}
