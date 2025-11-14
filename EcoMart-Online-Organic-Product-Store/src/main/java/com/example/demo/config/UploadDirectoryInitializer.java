package com.example.demo.config;

import com.example.demo.controller.ProductController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class UploadDirectoryInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Ensure uploads directory exists
        String uploadDir = ProductController.UPLOAD_DIRECTORY;
        File uploadFolder = new File(uploadDir);
        
        if (!uploadFolder.exists()) {
            boolean created = uploadFolder.mkdirs();
            if (created) {
                System.out.println("✅ Uploads directory created: " + uploadDir);
            } else {
                System.out.println("⚠️ Failed to create uploads directory: " + uploadDir);
            }
        } else {
            System.out.println("✅ Uploads directory already exists: " + uploadDir);
        }
    }
}

