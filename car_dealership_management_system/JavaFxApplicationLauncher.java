package com.cardealership.managementsystem;

import com.cardealership.managementsystem.ui.JavaFXApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplicationLauncher extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        // Create Spring context but avoid starting the web server
        this.applicationContext = new SpringApplicationBuilder(CarDealershipManagementSystemApplication.class)
                .headless(false)
                .web(WebApplicationType.NONE)  // Fixed: Use the enum instead of boolean
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) {
        JavaFXApplication javaFXApplication = applicationContext.getBean(JavaFXApplication.class);
        javaFXApplication.setPrimaryStage(primaryStage);
        javaFXApplication.showLoginScreen();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
}