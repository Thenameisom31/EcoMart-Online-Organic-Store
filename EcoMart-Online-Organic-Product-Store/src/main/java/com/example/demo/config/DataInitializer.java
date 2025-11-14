package com.example.demo.config;

import com.example.demo.service.DummyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DummyDataService dummyDataService;

    @Override
    public void run(String... args) throws Exception {
        dummyDataService.initializeDummyData();
        System.out.println("✅ Dummy data initialization completed!");
    }
}
