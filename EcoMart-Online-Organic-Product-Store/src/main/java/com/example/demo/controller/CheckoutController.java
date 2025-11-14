package com.example.demo.controller;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.User;
import com.example.demo.service.CartService;
import com.example.demo.service.CheckoutService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final CheckoutService checkoutService;

    public CheckoutController(CartService cartService, CheckoutService checkoutService) {
        this.cartService = cartService;
        this.checkoutService = checkoutService;
    }

    /** Show checkout page */
    @GetMapping
    public String checkoutPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartService.getCartItems(user);
        double total = cartItems.stream()
                .mapToDouble(item -> item.getTotalPrice() != null ? item.getTotalPrice() : 0.0)
                .sum();

        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", total);

        return "user/checkout";
    }

    /** Place order */
    @PostMapping("/place-order")
    public String placeOrder(
            HttpSession session,
            @RequestParam(required = false) String shippingAddress,
            @RequestParam(required = false) String shippingCity,
            @RequestParam(required = false) String shippingState,
            @RequestParam(required = false) String shippingZipCode,
            @RequestParam(required = false) String shippingPhone) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        checkoutService.processCheckout(user, shippingAddress, shippingCity, shippingState, shippingZipCode, shippingPhone);
        return "redirect:/user/orders?success";
    }
}
