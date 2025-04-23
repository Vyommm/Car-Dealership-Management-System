package com.cardealership.managementsystem.ui;

import com.cardealership.managementsystem.service.CarService;
import com.cardealership.managementsystem.service.CustomerService;
import com.cardealership.managementsystem.service.EmployeeService;
import com.cardealership.managementsystem.service.SaleService;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class JavaFXApplication {

    private Stage primaryStage;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CarService carService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SaleService saleService;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Car Dealership Management System");

        // Set close request event
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
    }

    public void showLoginScreen() {
        try {
            // Load the login screen
            FXMLLoader loader = new FXMLLoader();
            URL fxmlUrl = getClass().getResource("/fxml/login.fxml");
            loader.setLocation(fxmlUrl);
            loader.setControllerFactory(applicationContext::getBean);

            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);

            // Set stylesheet
            URL cssUrl = getClass().getResource("/static/css/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("CSS file not found");
            }

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDashboard() {
        try {
            // Load the dashboard
            FXMLLoader loader = new FXMLLoader();
            URL fxmlUrl = getClass().getResource("/fxml/dashboard.fxml");
            loader.setLocation(fxmlUrl);
            loader.setControllerFactory(applicationContext::getBean);

            Parent root = loader.load();
            Scene scene = new Scene(root, 1024, 768);

            // Set stylesheet
            URL cssUrl = getClass().getResource("/static/css/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper methods to load other screens
    public Parent loadCarInventoryScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL fxmlUrl = getClass().getResource("/fxml/car_inventory.fxml");
        loader.setLocation(fxmlUrl);
        loader.setControllerFactory(applicationContext::getBean);

        return loader.load();
    }

    public Parent loadCustomerManagementScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL fxmlUrl = getClass().getResource("/fxml/customer_management.fxml");
        loader.setLocation(fxmlUrl);
        loader.setControllerFactory(applicationContext::getBean);

        return loader.load();
    }

    public Parent loadSalesManagementScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL fxmlUrl = getClass().getResource("/fxml/sales_management.fxml");
        loader.setLocation(fxmlUrl);
        loader.setControllerFactory(applicationContext::getBean);

        return loader.load();
    }
}