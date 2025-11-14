package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/admin/logout")
    public String logoutAdmin(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    @GetMapping("/user/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}
