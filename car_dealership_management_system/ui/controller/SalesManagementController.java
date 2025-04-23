package com.cardealership.managementsystem.ui.controller;

import com.cardealership.managementsystem.exception.SaleProcessingException;
import com.cardealership.managementsystem.model.Car;
import com.cardealership.managementsystem.model.Customer;
import com.cardealership.managementsystem.model.Employee;
import com.cardealership.managementsystem.model.Sale;
import com.cardealership.managementsystem.service.CarService;
import com.cardealership.managementsystem.service.CustomerService;
import com.cardealership.managementsystem.service.EmployeeService;
import com.cardealership.managementsystem.service.SaleService;
import com.cardealership.managementsystem.ui.util.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SalesManagementController {

    @FXML
    private TableView<Sale> salesTable;

    @FXML
    private TableColumn<Sale, Long> colId;

    @FXML
    private TableColumn<Sale, String> colCar;

    @FXML
    private TableColumn<Sale, String> colCustomer;

    @FXML
    private TableColumn<Sale, String> colSalesperson;

    @FXML
    private TableColumn<Sale, LocalDate> colSaleDate;

    @FXML
    private TableColumn<Sale, BigDecimal> colSalePrice;

    @FXML
    private TableColumn<Sale, BigDecimal> colTax;

    @FXML
    private TableColumn<Sale, BigDecimal> colTotalPrice;

    @FXML
    private TableColumn<Sale, String> colPaymentMethod;

    @FXML
    private TableColumn<Sale, String> colStatus;

    @FXML
    private ComboBox<Car> cmbCar;

    @FXML
    private ComboBox<Customer> cmbCustomer;

    @FXML
    private ComboBox<Employee> cmbSalesperson;

    @FXML
    private DatePicker dateSaleDate;

    @FXML
    private TextField txtSalePrice;

    @FXML
    private TextField txtTax;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private ComboBox<String> cmbPaymentMethod;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private Button btnProcess;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;

    @FXML
    private DatePicker dateStartDate;

    @FXML
    private DatePicker dateEndDate;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnViewAll;

    @Autowired
    private SaleService saleService;

    @Autowired
    private CarService carService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UIUtils uiUtils;

    private ObservableList<Sale> salesList = FXCollections.observableArrayList();
    private ObservableList<Car> carsList = FXCollections.observableArrayList();
    private ObservableList<Customer> customersList = FXCollections.observableArrayList();
    private ObservableList<Employee> salesPersonsList = FXCollections.observableArrayList();
    private Sale selectedSale;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupComboBoxes();
        loadSales();
        setupEventHandlers();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCar.setCellValueFactory(cellData -> {
            Sale sale = cellData.getValue();
            if (sale.getCar() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> sale.getCar().getYear() + " " + sale.getCar().getMake() + " " + sale.getCar().getModel()
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "N/A");
        });
        colCustomer.setCellValueFactory(cellData -> {
            Sale sale = cellData.getValue();
            if (sale.getCustomer() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> sale.getCustomer().getFirstName() + " " + sale.getCustomer().getLastName()
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "N/A");
        });
        colSalesperson.setCellValueFactory(cellData -> {
            Sale sale = cellData.getValue();
            if (sale.getSalesperson() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> sale.getSalesperson().getFirstName() + " " + sale.getSalesperson().getLastName()
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "N/A");
        });
        colSaleDate.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colTax.setCellValueFactory(new PropertyValueFactory<>("tax"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("saleStatus"));
    }

    private void setupComboBoxes() {
        // Setup cars combo box
        loadAvailableCars();
        cmbCar.setItems(carsList);
        cmbCar.setConverter(new javafx.util.StringConverter<Car>() {
            @Override
            public String toString(Car car) {
                return car == null ? "" : car.getYear() + " " + car.getMake() + " " + car.getModel() + " - $" + car.getPrice();
            }

            @Override
            public Car fromString(String string) {
                return null; // Not needed for combo box
            }
        });

        // Setup customers combo box
        loadCustomers();
        cmbCustomer.setItems(customersList);
        cmbCustomer.setConverter(new javafx.util.StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                return customer == null ? "" : customer.getFirstName() + " " + customer.getLastName();
            }

            @Override
            public Customer fromString(String string) {
                return null; // Not needed for combo box
            }
        });

        // Setup salespeople combo box
        loadSalespeople();
        cmbSalesperson.setItems(salesPersonsList);
        cmbSalesperson.setConverter(new javafx.util.StringConverter<Employee>() {
            @Override
            public String toString(Employee employee) {
                return employee == null ? "" : employee.getFirstName() + " " + employee.getLastName();
            }

            @Override
            public Employee fromString(String string) {
                return null; // Not needed for combo box
            }
        });

        // Setup payment method combo box
        cmbPaymentMethod.getItems().addAll("Cash", "Credit Card", "Debit Card", "Bank Transfer", "Financing");

        // Setup status combo box
        cmbStatus.getItems().addAll("Pending", "Completed", "Cancelled");

        // Set default values
        dateSaleDate.setValue(LocalDate.now());
        cmbPaymentMethod.setValue("Cash");
        cmbStatus.setValue("Completed");
    }

    private void loadAvailableCars() {
        carsList.clear();
        carsList.addAll(carService.getAvailableCars());
    }

    private void loadCustomers() {
        customersList.clear();
        customersList.addAll(customerService.getAllCustomers());
    }

    private void loadSalespeople() {
        salesPersonsList.clear();
        // Filter only salespeople
        List<Employee> allEmployees = employeeService.findByPosition("Salesperson");
        salesPersonsList.addAll(allEmployees);
    }

    private void loadSales() {
        salesList.clear();
        salesList.addAll(saleService.getAllSales());
        salesTable.setItems(salesList);
    }

    private void setupEventHandlers() {
        // Handle sale selection
        salesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedSale = newSelection;
                displaySaleDetails(selectedSale);
            }
        });

        // Calculate total price when sale price or tax changes
        txtSalePrice.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice());
        txtTax.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice());
    }

    private void displaySaleDetails(Sale sale) {
        if (sale.getCar() != null) {
            // For updating existing sales, we need to include the sold car
            if (!carsList.contains(sale.getCar())) {
                carsList.add(sale.getCar());
            }
            cmbCar.setValue(sale.getCar());
        }

        cmbCustomer.setValue(sale.getCustomer());
        cmbSalesperson.setValue(sale.getSalesperson());
        dateSaleDate.setValue(sale.getSaleDate());
        txtSalePrice.setText(sale.getSalePrice().toString());
        txtTax.setText(sale.getTax().toString());
        lblTotalPrice.setText(sale.getTotalPrice().toString());
        cmbPaymentMethod.setValue(sale.getPaymentMethod());
        cmbStatus.setValue(sale.getSaleStatus());
    }

    private void calculateTotalPrice() {
        try {
            BigDecimal salePrice = uiUtils.parseBigDecimal(txtSalePrice.getText(), BigDecimal.ZERO);
            BigDecimal tax = uiUtils.parseBigDecimal(txtTax.getText(), BigDecimal.ZERO);
            BigDecimal totalPrice = salePrice.add(tax);
            lblTotalPrice.setText(totalPrice.toString());
        } catch (Exception e) {
            lblTotalPrice.setText("Error");
        }
    }

    @FXML
    private void handleProcessAction(ActionEvent event) {
        if (validateSaleInput()) {
            try {
                Car selectedCar = cmbCar.getValue();
                Customer selectedCustomer = cmbCustomer.getValue();
                Employee selectedSalesperson = cmbSalesperson.getValue();
                BigDecimal salePrice = uiUtils.parseBigDecimal(txtSalePrice.getText(), BigDecimal.ZERO);
                BigDecimal tax = uiUtils.parseBigDecimal(txtTax.getText(), BigDecimal.ZERO);
                String paymentMethod = cmbPaymentMethod.getValue();

                Sale processedSale = saleService.processSale(
                        selectedCar.getId(),
                        selectedCustomer.getId(),
                        selectedSalesperson.getId(),
                        salePrice,
                        tax,
                        paymentMethod
                );

                salesList.add(processedSale);
                clearFields();

                // Refresh available cars
                loadAvailableCars();

                uiUtils.showInfoAlert("Success", "Sale Processed", "Sale has been processed successfully.");
            } catch (Exception e) {
                uiUtils.showErrorAlert("Error", "Failed to process sale", e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        if (selectedSale == null) {
            uiUtils.showErrorAlert("Error", "No Sale Selected", "Please select a sale to update.");
            return;
        }

        if (validateSaleInput()) {
            try {
                selectedSale.setCar(cmbCar.getValue());
                selectedSale.setCustomer(cmbCustomer.getValue());
                selectedSale.setSalesperson(cmbSalesperson.getValue());
                selectedSale.setSaleDate(dateSaleDate.getValue());
                selectedSale.setSalePrice(uiUtils.parseBigDecimal(txtSalePrice.getText(), BigDecimal.ZERO));
                selectedSale.setTax(uiUtils.parseBigDecimal(txtTax.getText(), BigDecimal.ZERO));
                selectedSale.setPaymentMethod(cmbPaymentMethod.getValue());
                selectedSale.setSaleStatus(cmbStatus.getValue());

                Sale updatedSale = saleService.updateSale(selectedSale.getId(), selectedSale);

                // Update the list
                int index = salesList.indexOf(selectedSale);
                if (index >= 0) {
                    salesList.set(index, updatedSale);
                }

                uiUtils.showInfoAlert("Success", "Sale Updated", "Sale has been updated successfully.");
            } catch (Exception e) {
                uiUtils.showErrorAlert("Error", "Failed to update sale", e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        if (selectedSale == null) {
            uiUtils.showErrorAlert("Error", "No Sale Selected", "Please select a sale to delete.");
            return;
        }

        boolean confirm = uiUtils.showConfirmationAlert("Confirm Delete", "Delete Sale",
                "Are you sure you want to delete the selected sale? This action cannot be undone.");

        if (confirm) {
            try {
                saleService.deleteSale(selectedSale.getId());
                salesList.remove(selectedSale);
                clearFields();
                selectedSale = null;

                // Refresh available cars
                loadAvailableCars();

                uiUtils.showInfoAlert("Success", "Sale Deleted", "Sale has been deleted successfully.");
            } catch (Exception e) {
                uiUtils.showErrorAlert("Error", "Failed to delete sale", e.getMessage());
            }
        }
    }

    @FXML
    private void handleClearAction(ActionEvent event) {
        clearFields();
        selectedSale = null;
        salesTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        LocalDate startDate = dateStartDate.getValue();
        LocalDate endDate = dateEndDate.getValue();

        if (startDate == null || endDate == null) {
            uiUtils.showErrorAlert("Error", "Invalid Date Range", "Please select both start and end dates.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            uiUtils.showErrorAlert("Error", "Invalid Date Range", "Start date cannot be after end date.");
            return;
        }

        try {
            List<Sale> salesInRange = saleService.getSalesByDateRange(startDate, endDate);
            salesList.clear();
            salesList.addAll(salesInRange);
        } catch (Exception e) {
            uiUtils.showErrorAlert("Error", "Search Failed", e.getMessage());
        }
    }

    @FXML
    private void handleViewAllAction(ActionEvent event) {
        loadSales();
    }

    private boolean validateSaleInput() {
        if (cmbCar.getValue() == null) {
            uiUtils.showErrorAlert("Validation Error", "No Car Selected", "Please select a car for the sale.");
            return false;
        }

        if (cmbCustomer.getValue() == null) {
            uiUtils.showErrorAlert("Validation Error", "No Customer Selected", "Please select a customer for the sale.");
            return false;
        }

        if (cmbSalesperson.getValue() == null) {
            uiUtils.showErrorAlert("Validation Error", "No Salesperson Selected", "Please select a salesperson for the sale.");
            return false;
        }

        if (dateSaleDate.getValue() == null) {
            uiUtils.showErrorAlert("Validation Error", "No Sale Date", "Please select a date for the sale.");
            return false;
        }

        Map<String, TextInputControl> requiredFields = new HashMap<>();
        requiredFields.put("Sale Price", txtSalePrice);
        requiredFields.put("Tax", txtTax);

        if (!uiUtils.validateRequiredFields(requiredFields)) {
            return false;
        }

        // Validate numeric fields
        if (!uiUtils.validateNumericField("Sale Price", txtSalePrice.getText(),
                value -> {
                    try {
                        BigDecimal price = new BigDecimal(value);
                        return price.compareTo(BigDecimal.ZERO) > 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "Sale Price must be a valid number greater than 0")) {
            return false;
        }

        if (!uiUtils.validateNumericField("Tax", txtTax.getText(),
                value -> {
                    try {
                        BigDecimal tax = new BigDecimal(value);
                        return tax.compareTo(BigDecimal.ZERO) >= 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "Tax must be a valid number greater than or equal to 0")) {
            return false;
        }

        return true;
    }

    private void clearFields() {
        cmbCar.setValue(null);
        cmbCustomer.setValue(null);
        cmbSalesperson.setValue(null);
        dateSaleDate.setValue(LocalDate.now());
        txtSalePrice.clear();
        txtTax.clear();
        lblTotalPrice.setText("0.00");
        cmbPaymentMethod.setValue("Cash");
        cmbStatus.setValue("Completed");
    }
}