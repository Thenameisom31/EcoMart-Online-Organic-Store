package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class AdminInitializationService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "ps@gmail.com";
        String adminPassword = "admin@123";

        User existingAdmin = userRepository.findByEmail(adminEmail);
        
        if (existingAdmin == null) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(adminPassword);
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setMbo_No(0);
            
            userRepository.save(admin);
            System.out.println("✅ Admin user created successfully!");
            System.out.println("   Email: " + adminEmail);
            System.out.println("   Password: " + adminPassword);
        } else {
            System.out.println("✅ Admin user already exists!");
        }
    }
}
