package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserDashService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserDashController {

    @Autowired
    private UserDashService dashboardService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;

    /** ✅ User Dashboard */
    @GetMapping("/user/dashboard")
    public String userDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("categories", dashboardService.getAllCategories());
        return "user/user-dashboard";
    }

    /** ✅ All Products Page */
    @GetMapping("/user/products")
    public String viewProducts(Model model, HttpSession session) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);

        // Get cart from session
        @SuppressWarnings("unchecked")
        List<Long> cart = (List<Long>) session.getAttribute("cart");
        int cartCount = (cart != null) ? cart.size() : 0;
        model.addAttribute("cartCount", cartCount);

        // Pass product IDs in cart to Thymeleaf
        model.addAttribute("cartProductIds", cart != null ? cart : new ArrayList<>());

        return "user/view-products";
    }

    /** ✅ Profile Page */
    @GetMapping("/user/profile")
    public String userProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        // Fetch fresh user data from database
        User freshUser = userService.findByEmail(user.getEmail());
        if (freshUser != null) {
            model.addAttribute("user", freshUser);
            session.setAttribute("loggedUser", freshUser);
        } else {
            model.addAttribute("user", user);
        }
        return "user/profile";
    }

    /** ✅ Update Profile */
    @PostMapping("/user/profile/update")
    public String updateProfile(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("mbo_No") String mboNo,
            HttpSession session,
            Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        // Fetch user from database
        User dbUser = userService.findByEmail(user.getEmail());
        if (dbUser != null) {
            // Update user details
            dbUser.setFirstName(firstName);
            dbUser.setLastName(lastName);
            try {
                dbUser.setMbo_No(Integer.parseInt(mboNo));
            } catch (NumberFormatException e) {
                model.addAttribute("error", "Invalid mobile number format");
                model.addAttribute("user", dbUser);
                return "user/profile";
            }
            
            // Save updated user to database
            User updatedUser = userService.updateUser(dbUser);
            session.setAttribute("loggedUser", updatedUser);
            model.addAttribute("user", updatedUser);
            model.addAttribute("success", "Profile updated successfully!");
        } else {
            model.addAttribute("error", "User not found");
            model.addAttribute("user", user);
        }
        return "user/profile";
    }

    
 // Contact Us Page
    @GetMapping("/user/contact")
    public String contactPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "user/contact";  // this loads contact.html
    }

    // Handle form submit
    @PostMapping("/user/contact/submit")
    public String submitContact(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        // 📝 for now just log it or show success
        System.out.println("Contact message from " + name + ": " + message);

        model.addAttribute("user", user);
        model.addAttribute("success", "Message sent successfully!");
        return "user/contact";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // clear session
        return "redirect:/login?logout"; // redirect to login page
    }

    @GetMapping("/user/category/{id}/products")
    public String viewProductsByCategory(@PathVariable("id") Long categoryId,
                                         Model model,
                                         HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("categories", dashboardService.getAllCategories());
        model.addAttribute("products", dashboardService.getProductsByCategory(categoryId));
        model.addAttribute("selectedCategoryId", categoryId);

        // ✅ Add cart info (to avoid null in Thymeleaf)
        @SuppressWarnings("unchecked")
        List<Long> cart = (List<Long>) session.getAttribute("cart");
        int cartCount = (cart != null) ? cart.size() : 0;
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("cartProductIds", cart != null ? cart : new ArrayList<>());

        return "user/view-products";
    }







}
