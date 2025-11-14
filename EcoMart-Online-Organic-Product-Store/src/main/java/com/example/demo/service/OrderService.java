package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByUser(User user);
    void placeOrder(User user);
    void placeOrder(User user, String shippingAddress, String shippingCity, String shippingState, String shippingZipCode, String shippingPhone);
    void cancelOrder(Long orderId, User user);
    
    List<Order> getAllOrders();
    void updateOrderStatus(Long orderId, String status);
	Order getOrderById(Long id);
	void updateOrder(Long id, Order updatedOrder);
	long countAllOrders();
}
