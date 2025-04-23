package com.cardealership.managementsystem;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.cardealership.managementsystem")
public class CarDealershipManagementSystemApplication {

    public static void main(String[] args) {
        // Launch the JavaFX application
        Application.launch(JavaFxApplicationLauncher.class, args);
    }
}