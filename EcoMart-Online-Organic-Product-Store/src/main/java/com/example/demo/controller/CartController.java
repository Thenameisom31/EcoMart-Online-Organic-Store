package com.example.demo.controller;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    /** 🔹 Helper to update cart count in session */
    private void updateCartCount(User user, HttpSession session) {
        int cartCount = cartService.getCartCount(user);
        session.setAttribute("cartCount", cartCount);
    }

    /** ✅ View Cart */
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
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

        // ✅ keep session cart count updated when viewing cart
        updateCartCount(user, session);

        return "user/cart"; // loads cart.html
    }

    /** ✅ Add Product to Cart */
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        Product product = productService.getProductById(productId);
        cartService.addToCart(user, product, quantity);

        // ✅ Update cart count
        updateCartCount(user, session);

        redirectAttributes.addFlashAttribute("success",
                product.getName() + " added to cart!");

        return "redirect:/user/products";
    }

    /** ✅ Clear Entire Cart */
    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        cartService.clearCart(user);

        // ✅ Update cart count
        updateCartCount(user, session);

        redirectAttributes.addFlashAttribute("success", "Cart cleared successfully!");
        return "redirect:/user/cart";
    }

    /** ✅ Update Quantity */
    @PostMapping("/update/{productId}")
    public String updateCartQuantity(@PathVariable Long productId,
                                     @RequestParam int quantity,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        cartService.updateQuantity(user, productId, quantity);

        // ✅ Update cart count
        updateCartCount(user, session);

        redirectAttributes.addFlashAttribute("success", "Cart updated successfully!");
        return "redirect:/user/cart";
    }
}
