package com.cardealership.managementsystem.ui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class UIUtils {

    /**
     * Show an information alert dialog
     *
     * @param title The dialog title
     * @param header The header text
     * @param content The content text
     */
    public void showInfoAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Show an error alert dialog
     *
     * @param title The dialog title
     * @param header The header text
     * @param content The content text
     */
    public void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Show a confirmation alert dialog
     *
     * @param title The dialog title
     * @param header The header text
     * @param content The content text
     * @return true if the user clicked OK, false otherwise
     */
    public boolean showConfirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Validate required fields
     *
     * @param fieldsMap Map of field label and field value
     * @return true if all fields are valid, false otherwise
     */
    public boolean validateRequiredFields(Map<String, TextInputControl> fieldsMap) {
        StringBuilder errorMessage = new StringBuilder("The following fields are required:\n");
        boolean hasEmptyField = false;

        for (Map.Entry<String, TextInputControl> entry : fieldsMap.entrySet()) {
            if (entry.getValue().getText().trim().isEmpty()) {
                errorMessage.append("- ").append(entry.getKey()).append("\n");
                hasEmptyField = true;
            }
        }

        if (hasEmptyField) {
            showErrorAlert("Validation Error", "Required Fields Missing", errorMessage.toString());
            return false;
        }

        return true;
    }

    /**
     * Parse a string to an integer
     *
     * @param value The string value
     * @param defaultValue The default value if parsing fails
     * @return The parsed integer or default value
     */
    public Integer parseInteger(String value, Integer defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Parse a string to a BigDecimal
     *
     * @param value The string value
     * @param defaultValue The default value if parsing fails
     * @return The parsed BigDecimal or default value
     */
    public BigDecimal parseBigDecimal(String value, BigDecimal defaultValue) {
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Validate a numeric field
     *
     * @param fieldLabel The field label
     * @param fieldValue The field value
     * @param predicate The validation predicate
     * @param errorMessage The error message
     * @return true if field is valid, false otherwise
     */
    public boolean validateNumericField(String fieldLabel, String fieldValue, Predicate<String> predicate, String errorMessage) {
        if (!predicate.test(fieldValue)) {
            showErrorAlert("Validation Error", "Invalid " + fieldLabel, errorMessage);
            return false;
        }
        return true;
    }

    /**
     * Clear all text fields
     *
     * @param fields The array of text fields
     */
    public void clearFields(TextInputControl... fields) {
        for (TextInputControl field : fields) {
            field.clear();
        }
    }

    /**
     * Center a stage on screen
     *
     * @param stage The stage to center
     */
    public void centerStage(Stage stage) {
        stage.centerOnScreen();
    }
}