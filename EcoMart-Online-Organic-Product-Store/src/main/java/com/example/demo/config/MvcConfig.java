package com.example.demo.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.controller.ProductController;

import java.io.File;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ensure uploads directory exists
        String uploadDir = ProductController.UPLOAD_DIRECTORY;
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        
        // Handle Windows paths correctly
        String uploadPath = Paths.get(uploadDir).toAbsolutePath().toString();
        // Normalize path separators for Windows
        if (File.separator.equals("\\")) {
            uploadPath = uploadPath.replace("\\", "/");
        }
        
        // Add resource handler for uploaded images
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
