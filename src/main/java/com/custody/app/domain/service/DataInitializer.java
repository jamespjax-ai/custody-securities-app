package com.custody.app.domain.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StaticDataService staticDataService;

    public DataInitializer(StaticDataService staticDataService) {
        this.staticDataService = staticDataService;
    }

    @Override
    public void run(String... args) throws Exception {
        staticDataService.seedInitialData();
        System.out.println("Static data seeded successfully.");
    }
}
