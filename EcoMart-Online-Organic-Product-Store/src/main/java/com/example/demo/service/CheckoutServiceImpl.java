package com.example.demo.service;

import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutServiceImpl(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @Override
    public void processCheckout(User user) {
        processCheckout(user, null, null, null, null, null);
    }

    @Override
    public void processCheckout(User user, String shippingAddress, String shippingCity, String shippingState, String shippingZipCode, String shippingPhone) {
        // Place order with shipping address
        orderService.placeOrder(user, shippingAddress, shippingCity, shippingState, shippingZipCode, shippingPhone);

        // Clear cart
        cartService.clearCart(user);
    }
}
