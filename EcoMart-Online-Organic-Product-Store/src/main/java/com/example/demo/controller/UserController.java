package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository;

    // Root route redirects to index
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    // Index page (landing page)
    @GetMapping("/index")
    public String showIndexPage() {
        return "index";
    }

    // Registration page
    @GetMapping("/register")
    public String openRegister(Model model) {
        model.addAttribute("user", new User());
        return "register"; 
    }

    // Handle registration
    @PostMapping("/inserted")
    public String doRegister(@ModelAttribute("user") User user, Model model) {
        boolean valid = service.doResiter(user);
        if (valid) {
            model.addAttribute("succMsg", "Registration successful!");
            return "login"; 
        } else {
            model.addAttribute("errorMes", "This email is already registered. Please try another.");
            return "register"; 
        }
    }

    // Login page
    @GetMapping("/login")
    public String openLogin(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    // Handle login
    @PostMapping("/done")
    public String doneLogin(@ModelAttribute("user") User user, Model model, HttpSession session) {
        User u = service.dologin(user.getEmail(), user.getPassword());

        if (u != null) {
            session.setAttribute("loggedUser", u);  // ✅ save user in session

            if ("ps@gmail.com".equalsIgnoreCase(u.getEmail())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/user/dashboard";
            }
        }
        model.addAttribute("errorMes", "Invalid credentials! Try again.");
        return "login";
    }
    
    @GetMapping("/admin/users")
    public String listAllUsers(Model model) {
        List<User> users = userRepository.findAll(); // or service.getAllUsers()
        model.addAttribute("users", users);
        return "admin/admin-users";
    }
}
