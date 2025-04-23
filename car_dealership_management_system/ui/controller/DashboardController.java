package com.cardealership.managementsystem.ui.controller;

import com.cardealership.managementsystem.service.CarService;
import com.cardealership.managementsystem.service.CustomerService;
import com.cardealership.managementsystem.service.EmployeeService;
import com.cardealership.managementsystem.service.SaleService;
import com.cardealership.managementsystem.ui.JavaFXApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DashboardController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Button btnCarInventory;

    @FXML
    private Button btnCustomerManagement;

    @FXML
    private Button btnSalesManagement;

    @FXML
    private Button btnLogout;

    @FXML
    private Label lblAvailableCars;

    @FXML
    private Label lblTotalCustomers;

    @FXML
    private Label lblTotalSales;

    @FXML
    private Label lblTodaySales;

    @FXML
    private BarChart<String, Number> salesChart;

    @FXML
    private PieChart inventoryChart;

    @Autowired
    private CarService carService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private JavaFXApplication javaFXApplication;

    @FXML
    public void initialize() {
        updateDashboardMetrics();
    }

    private void updateDashboardMetrics() {
        // Update metrics
        lblAvailableCars.setText(String.valueOf(carService.getAvailableCars().size()));
        lblTotalCustomers.setText(String.valueOf(customerService.getAllCustomers().size()));
        lblTotalSales.setText(String.valueOf(saleService.getAllSales().size()));
        lblTodaySales.setText(String.valueOf(saleService.getTodaySales().size()));

        // Update charts
        updateSalesChart();
        updateInventoryChart();
    }

    private void updateSalesChart() {
        // Implement sales chart data population
    }

    private void updateInventoryChart() {
        // Implement inventory chart data population
    }

    @FXML
    private void handleCarInventoryAction(ActionEvent event) throws IOException {
        Parent carInventoryRoot = javaFXApplication.loadCarInventoryScreen();
        mainBorderPane.setCenter(carInventoryRoot);
    }

    @FXML
    private void handleCustomerManagementAction(ActionEvent event) throws IOException {
        Parent customerManagementRoot = javaFXApplication.loadCustomerManagementScreen();
        mainBorderPane.setCenter(customerManagementRoot);
    }

    @FXML
    private void handleSalesManagementAction(ActionEvent event) throws IOException {
        Parent salesManagementRoot = javaFXApplication.loadSalesManagementScreen();
        mainBorderPane.setCenter(salesManagementRoot);
    }

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        // Redirect to login screen
        javaFXApplication.showLoginScreen();
    }
}