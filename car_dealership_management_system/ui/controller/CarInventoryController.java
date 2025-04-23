package com.cardealership.managementsystem.ui.controller;

import com.cardealership.managementsystem.model.Car;
import com.cardealership.managementsystem.service.CarService;
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
import java.util.Map;

@Component
public class CarInventoryController {

    @FXML
    private TableView<Car> carTable;

    @FXML
    private TableColumn<Car, Long> colId;

    @FXML
    private TableColumn<Car, String> colMake;

    @FXML
    private TableColumn<Car, String> colModel;

    @FXML
    private TableColumn<Car, Integer> colYear;

    @FXML
    private TableColumn<Car, String> colVin;

    @FXML
    private TableColumn<Car, String> colColor;

    @FXML
    private TableColumn<Car, String> colCondition;

    @FXML
    private TableColumn<Car, BigDecimal> colPrice;

    @FXML
    private TableColumn<Car, Integer> colMileage;

    @FXML
    private TableColumn<Car, Boolean> colSold;

    @FXML
    private TextField txtMake;

    @FXML
    private TextField txtModel;

    @FXML
    private TextField txtYear;

    @FXML
    private TextField txtVin;

    @FXML
    private TextField txtColor;

    @FXML
    private ComboBox<String> cmbCondition;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtMileage;

    @FXML
    private CheckBox chkSold;

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

    @Autowired
    private CarService carService;

    @Autowired
    private UIUtils uiUtils;

    private ObservableList<Car> carsList = FXCollections.observableArrayList();
    private Car selectedCar;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupComboBoxes();
        loadCars();
        setupEventHandlers();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colVin.setCellValueFactory(new PropertyValueFactory<>("vin"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colCondition.setCellValueFactory(new PropertyValueFactory<>("condition"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colMileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        colSold.setCellValueFactory(new PropertyValueFactory<>("sold"));
    }

    private void setupComboBoxes() {
        cmbCondition.getItems().addAll("New", "Used", "Certified Pre-Owned");
        cmbCondition.setValue("New");

        cmbSearchFilter.getItems().addAll("Make", "Model", "Year", "VIN", "Price Range", "Condition");
        cmbSearchFilter.setValue("Make");
    }

    private void loadCars() {
        carsList.clear();
        carsList.addAll(carService.getAllCars());
        carTable.setItems(carsList);
    }

    private void setupEventHandlers() {
        // Handle car selection
        carTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCar = newSelection;
                displayCarDetails(selectedCar);
            }
        });
    }

    private void displayCarDetails(Car car) {
        txtMake.setText(car.getMake());
        txtModel.setText(car.getModel());
        txtYear.setText(car.getYear().toString());
        txtVin.setText(car.getVin());
        txtColor.setText(car.getColor());
        cmbCondition.setValue(car.getCondition());
        txtPrice.setText(car.getPrice().toString());
        txtMileage.setText(car.getMileage().toString());
        chkSold.setSelected(car.getSold());
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        if (validateCarInput()) {
            try {
                Car newCar = new Car(
                        txtMake.getText().trim(),
                        txtModel.getText().trim(),
                        uiUtils.parseInteger(txtYear.getText(), 0),
                        txtVin.getText().trim(),
                        txtColor.getText().trim(),
                        cmbCondition.getValue(),
                        uiUtils.parseBigDecimal(txtPrice.getText(), BigDecimal.ZERO),
                        uiUtils.parseInteger(txtMileage.getText(), 0)
                );
                newCar.setSold(chkSold.isSelected());

                Car savedCar = carService.saveCar(newCar);
                carsList.add(savedCar);
                clearFields();
                uiUtils.showInfoAlert("Success", "Car Added", "Car has been added successfully.");
            } catch (Exception e) {
                uiUtils.showErrorAlert("Error", "Failed to add car", e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        if (selectedCar == null) {
            uiUtils.showErrorAlert("Error", "No Car Selected", "Please select a car to update.");
            return;
        }

        if (validateCarInput()) {
            try {
                selectedCar.setMake(txtMake.getText().trim());
                selectedCar.setModel(txtModel.getText().trim());
                selectedCar.setYear(uiUtils.parseInteger(txtYear.getText(), 0));
                selectedCar.setVin(txtVin.getText().trim());
                selectedCar.setColor(txtColor.getText().trim());
                selectedCar.setCondition(cmbCondition.getValue());
                selectedCar.setPrice(uiUtils.parseBigDecimal(txtPrice.getText(), BigDecimal.ZERO));
                selectedCar.setMileage(uiUtils.parseInteger(txtMileage.getText(), 0));
                selectedCar.setSold(chkSold.isSelected());

                Car updatedCar = carService.updateCar(selectedCar.getId(), selectedCar);

                // Update the list
                int index = carsList.indexOf(selectedCar);
                if (index >= 0) {
                    carsList.set(index, updatedCar);
                }

                uiUtils.showInfoAlert("Success", "Car Updated", "Car has been updated successfully.");
            } catch (Exception e) {
                uiUtils.showErrorAlert("Error", "Failed to update car", e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        if (selectedCar == null) {
            uiUtils.showErrorAlert("Error", "No Car Selected", "Please select a car to delete.");
            return;
        }

        boolean confirm = uiUtils.showConfirmationAlert("Confirm Delete", "Delete Car",
                "Are you sure you want to delete the selected car? This action cannot be undone.");

        if (confirm) {
            try {
                carService.deleteCar(selectedCar.getId());
                carsList.remove(selectedCar);
                clearFields();
                selectedCar = null;
                uiUtils.showInfoAlert("Success", "Car Deleted", "Car has been deleted successfully.");
            } catch (Exception e) {
                uiUtils.showErrorAlert("Error", "Failed to delete car", e.getMessage());
            }
        }
    }

    @FXML
    private void handleClearAction(ActionEvent event) {
        clearFields();
        selectedCar = null;
        carTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            loadCars();
            return;
        }

        String filter = cmbSearchFilter.getValue();
        try {
            carsList.clear();

            switch (filter) {
                case "Make":
                    carsList.addAll(carService.getCarsByMake(searchTerm));
                    break;
                case "Model":
                    carsList.addAll(carService.getCarsByModel(searchTerm));
                    break;
                case "Year":
                    Integer year = uiUtils.parseInteger(searchTerm, 0);
                    if (year > 0) {
                        carsList.addAll(carService.getCarsByYear(year));
                    }
                    break;
                case "VIN":
                    try {
                        Car car = carService.getCarByVin(searchTerm);
                        carsList.add(car);
                    } catch (Exception e) {
                        // No car found with the VIN
                    }
                    break;
                case "Price Range":
                    // Expecting a price range format: min-max
                    String[] range = searchTerm.split("-");
                    if (range.length == 2) {
                        BigDecimal minPrice = uiUtils.parseBigDecimal(range[0], BigDecimal.ZERO);
                        BigDecimal maxPrice = uiUtils.parseBigDecimal(range[1], new BigDecimal("1000000"));
                        carsList.addAll(carService.getCarsByPriceRange(minPrice, maxPrice));
                    }
                    break;
                case "Condition":
                    carsList.addAll(carService.getCarsByCondition(searchTerm));
                    break;
                default:
                    loadCars();
                    break;
            }
        } catch (Exception e) {
            uiUtils.showErrorAlert("Error", "Search Failed", e.getMessage());
        }
    }

    private boolean validateCarInput() {
        Map<String, TextInputControl> requiredFields = new HashMap<>();
        requiredFields.put("Make", txtMake);
        requiredFields.put("Model", txtModel);
        requiredFields.put("Year", txtYear);
        requiredFields.put("VIN", txtVin);
        requiredFields.put("Price", txtPrice);

        if (!uiUtils.validateRequiredFields(requiredFields)) {
            return false;
        }

        // Validate numeric fields
        if (!uiUtils.validateNumericField("Year", txtYear.getText(),
                value -> {
                    try {
                        int year = Integer.parseInt(value);
                        return year >= 1900 && year <= LocalDate.now().getYear() + 1;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "Year must be a valid number between 1900 and " + (LocalDate.now().getYear() + 1))) {
            return false;
        }

        if (!uiUtils.validateNumericField("Price", txtPrice.getText(),
                value -> {
                    try {
                        BigDecimal price = new BigDecimal(value);
                        return price.compareTo(BigDecimal.ZERO) >= 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "Price must be a valid number greater than or equal to 0")) {
            return false;
        }

        if (!txtMileage.getText().trim().isEmpty() &&
                !uiUtils.validateNumericField("Mileage", txtMileage.getText(),
                        value -> {
                            try {
                                int mileage = Integer.parseInt(value);
                                return mileage >= 0;
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        },
                        "Mileage must be a valid number greater than or equal to 0")) {
            return false;
        }

        return true;
    }

    private void clearFields() {
        txtMake.clear();
        txtModel.clear();
        txtYear.clear();
        txtVin.clear();
        txtColor.clear();
        cmbCondition.setValue("New");
        txtPrice.clear();
        txtMileage.clear();
        chkSold.setSelected(false);
    }
}