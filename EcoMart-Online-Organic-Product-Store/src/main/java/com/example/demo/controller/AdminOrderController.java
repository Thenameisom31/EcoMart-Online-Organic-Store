package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;

@Controller
@RequestMapping("/admin/orders")   // For ADMIN
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/admin-orders";
    }

    @GetMapping("/update/{id}")
    public String showUpdateOrderPage(@PathVariable("id") Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/admin/orders?error=notfound";
        }
        model.addAttribute("order", order);
        return "admin/update-order";
    }

    @PostMapping("/update/{id}")
    public String updateOrder(@PathVariable Long id, 
                             @ModelAttribute("order") Order updatedOrder,
                             @RequestParam(value = "status", required = false) String status) {
        if (status != null && !status.isEmpty()) {
            orderService.updateOrderStatus(id, status);
        } else {
            orderService.updateOrder(id, updatedOrder);
        }
        return "redirect:/admin/orders?updated";
    }
}
