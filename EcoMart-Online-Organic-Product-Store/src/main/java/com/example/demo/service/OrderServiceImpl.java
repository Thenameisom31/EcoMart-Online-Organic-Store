package com.example.demo.service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    // ---------- USER SIDE ----------
    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public void placeOrder(User user) {
        placeOrder(user, null, null, null, null, null);
    }

    @Override
    public void placeOrder(User user, String shippingAddress, String shippingCity, String shippingState, String shippingZipCode, String shippingPhone) {
        List<CartItem> cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot place order!");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("Pending");
        order.setDate(LocalDateTime.now());
        
        // Set shipping address if provided
        if (shippingAddress != null && !shippingAddress.trim().isEmpty()) {
            order.setShippingAddress(shippingAddress);
            order.setShippingCity(shippingCity);
            order.setShippingState(shippingState);
            order.setShippingZipCode(shippingZipCode);
            order.setShippingPhone(shippingPhone);
        }

        double total = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(cartItem.getProduct());
            oi.setQuantity(cartItem.getQuantity());
            oi.setTotalPrice(cartItem.getTotalPrice());

            total += cartItem.getTotalPrice();
            orderItems.add(oi);
        }

        order.setTotalAmount(total);
        order.setOrderItems(orderItems);

        orderRepository.save(order);

        cartService.clearCart(user);
    }

    @Override
    public void cancelOrder(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!Objects.equals(order.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized action");
        }

        order.setStatus("Cancelled");
        orderRepository.save(order);
    }

    // ---------- ADMIN SIDE ----------
    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(o -> {
            o.getUser().getFirstName();
            o.getOrderItems().forEach(oi -> oi.getProduct().getName());
        });
        return orders;
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public void updateOrder(Long id, Order updatedOrder) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        existingOrder.setStatus(updatedOrder.getStatus());
        existingOrder.setTotalAmount(updatedOrder.getTotalAmount());

        if (updatedOrder.getOrderItems() != null) {
            existingOrder.getOrderItems().clear();
            for (OrderItem oi : updatedOrder.getOrderItems()) {
                oi.setOrder(existingOrder);
                existingOrder.getOrderItems().add(oi);
            }
        }

        orderRepository.save(existingOrder);
    }

    // ---------- DASHBOARD ----------
    @Override
    public long countAllOrders() {
        return orderRepository.count();
    }
}
