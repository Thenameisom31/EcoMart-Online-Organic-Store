package com.example.demo.service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    /** ✅ Get all cart items for a user */
    @Override
    public List<CartItem> getCartItems(User user) {
        return cartRepository.findByUser(user);
    }

    /** ✅ Add product to cart */
    @Override
    public void addToCart(User user, Product product, int quantity) {
        List<CartItem> cartItems = cartRepository.findByUser(user);

        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                item.setTotalPrice(item.getQuantity() * product.getPrice());
                cartRepository.save(item);
                return;
            }
        }

        CartItem newItem = new CartItem();
        newItem.setUser(user);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        newItem.setTotalPrice(product.getPrice() * quantity);
        cartRepository.save(newItem);
    }

    /** ✅ Remove a specific product from cart */
    @Override
    public void removeFromCart(User user, Product product) {
        cartRepository.deleteByUserAndProduct(user, product);
    }

    /** ✅ Clear entire cart */
    @Override
    public void clearCart(User user) {
        List<CartItem> cartItems = cartRepository.findByUser(user);
        cartRepository.deleteAll(cartItems);
    }

    /** ✅ Update quantity of a product in cart */
    @Override
    public void updateQuantity(User user, Long productId, int quantity) {
        Product product = new Product();
        product.setId(productId);

        CartItem cartItem = cartRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(cartItem.getProduct().getPrice() * quantity);
        cartRepository.save(cartItem);
    }

    /** ✅ Get total cart count (sum of quantities) */
    @Override
    public int getCartCount(User user) {
        List<CartItem> cartItems = cartRepository.findByUser(user);
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }
}
