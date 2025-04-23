package com.cardealership.managementsystem.ui.controller;

import com.cardealership.managementsystem.exception.CustomerNotFoundException;
import com.cardealership.managementsystem.model.Customer;
import com.cardealership.managementsystem.model.Sale;
import com.cardealership.managementsystem.service.CustomerService;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomerManagementController {

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Long> colId;

    @FXML
    private TableColumn<Customer, String> colFirstName;

    @FXML
    private TableColumn<Customer, String> colLastName;

    @FXML
    private TableColumn<Customer, String> colEmail;

    @FXML
    private TableColumn<Customer, String> colPhone;

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private TableColumn<Customer, LocalDate> colRegistrationDate;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtAddress;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSearch;

    @FXML
    private ComboBox<String> cmbSearchFilter;

    @FXML
    private TableView<Sale> purchasesTable;

    @FXML
    private TableColumn<Sale, Long> colSaleId;

    @FXML
    private TableColumn<Sale, String> colCarDetails;

    @FXML
    private TableColumn<Sale, LocalDate> colSaleDate;

    @FXML
    private TableColumn<Sale, Double> colSaleAmount;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private UIUtils uiUtils;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private ObservableList<Sale> purchasesList = FXCollections.observableArrayList();
    private Customer selectedCustomer;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupSearchFilter();
        loadCustomers();
        setupEventHandlers();
    }

    private void setupTableColumns() {
        // Setup main customer table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colRegistrationDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        // Setup purchases table columns
        colSaleId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCarDetails.setCellValueFactory(cellData -> {
            Sale sale = cellData.getValue();
            if (sale.getCar() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> sale.getCar().getYear() + " " + sale.getCar().getMake() + " " + sale.getCar().getModel()
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "N/A");
        });
        colSaleDate.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        colSaleAmount.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    private void setupSearchFilter() {
        cmbSearchFilter.getItems().addAll("Name", "Email", "Phone");
        cmbSearchFilter.setValue("Name");
    }

    private void loadCustomers() {
        customerList.clear();
        customerList.addAll(customerService.getAllCustomers());
        customerTable.setItems(customerList);
    }

    private void setupEventHandlers() {
        // Handle customer selection
        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCustomer = newSelection;
                displayCustomerDetails(selectedCustomer);
                loadCustomerPurchases(selectedCustomer);
            }
        });
    }

    private void displayCustomerDetails(Customer customer) {
        txtFirstName.setText(customer.getFirstName());
        txtLastName.setText(customer.getLastName());
        txtEmail.setText(customer.getEmail());
        txtPhone.setText(customer.getPhone());
        txtAddress.setText(customer.getAddress());
    }

    private void loadCustomerPurchases(Customer customer) {
        purchasesList.clear();
        try {
            List<Sale> sales = saleService.getSalesByCustomer(customer.getId());
            purchasesList.addAll(sales);
            purchasesTable.setItems(purchasesList);
        } catch (Exception e) {
            uiUtils.showErrorAlert("Error", "Failed to load customer purchases", e.getMessage());
        }
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        if (validateCustomerInput()) {
            try {
                Customer newCustomer = new Customer(
                        txtFirstName.getText().trim(),
                        txtLastName.getText().trim(),
                        txtEmail.getText().trim(),
                        txtPhone.getText().trim(),
                        txtAddress.getText().trim()
                );

                Customer savedCustomer = customerService.saveCustomer(newCustomer);
                customerList.add(savedCustomer);
                clearFields();
                uiUtils.showInfoAlert("Success", "Customer Added", "Customer has been added successfully.");
            } catch (Exception e) {
                uiUtils.showErrorAlert("Error", "Failed to add customer", e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        if (selectedCustomer == null) {
            uiUtils.showErrorAlert("Error", "No Customer Selected", "Please select a customer to update.");
            return;
        }

        if (validateCustomerInput()) {
            try {
                selectedCustomer.setFirstName(txtFirstName.getText().trim());
                selectedCustomer.setLastName(txtLastName.getText().trim());
                selectedCustomer.setEmail(txtEmail.getText().trim());
                selectedCustomer.setPhone(txtPhone.getText().trim());
                selectedCustomer.setAddress(txtAddress.getText().trim());

                Customer updatedCustomer = customerService.updateCustomer(selectedCustomer.getId(), selectedCustomer);

                // Update the list
                int index = customerList.indexOf(selectedCustomer);
                if (index >= 0) {
                    customerList.set(index, updatedCustomer);
                }

                uiUtils.showInfoAlert("Success", "Customer Updated", "Customer has been updated successfully.");
            } catch (Exception e) {
                uiUtils.showErrorAlert("Error", "Failed to update customer", e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        if (selectedCustomer == null) {
            uiUtils.showErrorAlert("Error", "No Customer Selected", "Please select a customer to delete.");
            return;
        }

        boolean confirm = uiUtils.showConfirmationAlert("Confirm Delete", "Delete Customer",
                "Are you sure you want to delete the selected customer? This action cannot be undone.");

        if (confirm) {
            try {
                customerService.deleteCustomer(selectedCustomer.getId());
                customerList.remove(selectedCustomer);
                clearFields();
                selectedCustomer = null;
                uiUtils.showInfoAlert("Success", "Customer Deleted", "Customer has been deleted successfully.");
            } catch (Exception e) {
                uiUtils.showErrorAlert("Error", "Failed to delete customer", e.getMessage());
            }
        }
    }

    @FXML
    private void handleClearAction(ActionEvent event) {
        clearFields();
        selectedCustomer = null;
        customerTable.getSelectionModel().clearSelection();
        purchasesList.clear();
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            loadCustomers();
            return;
        }

        String filter = cmbSearchFilter.getValue();
        try {
            customerList.clear();

            if ("Name".equals(filter)) {
                // Assuming the search term contains first name and last name separated by space
                String[] nameParts = searchTerm.split("\\s+", 2);
                String firstName = nameParts[0];
                String lastName = nameParts.length > 1 ? nameParts[1] : null;
                customerList.addAll(customerService.findByName(firstName, lastName));
            } else if ("Email".equals(filter)) {
                try {
                    Customer customer = customerService.findByEmail(searchTerm);
                    customerList.add(customer);
                } catch (CustomerNotFoundException e) {
                    // No customer found with the email
                }
            } else if ("Phone".equals(filter)) {
                try {
                    Customer customer = customerService.findByPhone(searchTerm);
                    customerList.add(customer);
                } catch (CustomerNotFoundException e) {
                    // No customer found with the phone
                }
            }
        } catch (Exception e) {
            uiUtils.showErrorAlert("Error", "Search Failed", e.getMessage());
        }
    }

    private boolean validateCustomerInput() {
        Map<String, TextInputControl> requiredFields = new HashMap<>();
        requiredFields.put("First Name", txtFirstName);
        requiredFields.put("Last Name", txtLastName);
        requiredFields.put("Email", txtEmail);
        requiredFields.put("Phone", txtPhone);

        return uiUtils.validateRequiredFields(requiredFields);
    }

    private void clearFields() {
        txtFirstName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
    }
}