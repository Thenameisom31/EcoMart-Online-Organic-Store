package com.example.demo.service;

import com.example.demo.entity.User;

public interface CheckoutService {
    void processCheckout(User user);
    void processCheckout(User user, String shippingAddress, String shippingCity, String shippingState, String shippingZipCode, String shippingPhone);
}
