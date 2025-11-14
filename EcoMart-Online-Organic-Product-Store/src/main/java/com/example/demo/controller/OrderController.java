package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/orders")   // Base path for USER orders
public class OrderController {

    @Autowired
    private OrderService orderService;

    /** ================= USER ORDERS ================= */

    // Show logged-in user's orders (GET /user/orders)
    @GetMapping
    public String listUserOrders(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "user/my-orders";  // ✅ templates/user/my-orders.html
    }

    // Place order (POST /user/orders/place)
    @PostMapping("/place")
    public String placeOrder(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        orderService.placeOrder(user);
        return "redirect:/user/orders?success";   // ✅ redirect correctly
    }

    // Cancel order (POST /user/orders/cancel/{id})
    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        orderService.cancelOrder(id, user);
        return "redirect:/user/orders?cancelled";  // ✅ redirect correctly
    }
    

}
