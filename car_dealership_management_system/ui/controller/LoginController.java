package com.cardealership.managementsystem.ui.controller;

import com.cardealership.managementsystem.ui.JavaFXApplication;
import com.cardealership.managementsystem.ui.util.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @Autowired
    private JavaFXApplication javaFXApplication;

    @Autowired
    private UIUtils uiUtils;

    @Autowired(required = false)
    private AuthenticationManager authenticationManager;

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            uiUtils.showErrorAlert("Login Failed", "Empty Credentials", "Please enter both username and password.");
            return;
        }

        try {
            // For development, add a simplified authentication option
            if (isDevelopmentMode()) {
                // Simple validation for development
                if (("admin".equals(username) && "admin".equals(password)) ||
                        ("user".equals(username) && "password".equals(password))) {
                    // Login successful
                    javaFXApplication.showDashboard();
                } else {
                    uiUtils.showErrorAlert("Login Failed", "Invalid Credentials", "Please check your username and password.");
                }
            } else {
                // Spring Security authentication for production
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                javaFXApplication.showDashboard();
            }
        } catch (AuthenticationException e) {
            uiUtils.showErrorAlert("Login Failed", "Authentication Error", "Invalid username or password.");
        } catch (Exception e) {
            uiUtils.showErrorAlert("Error", "Login Error", "An error occurred during login: " + e.getMessage());
        }
    }

    private boolean isDevelopmentMode() {
        // Check if we're in development mode (can be based on Spring profiles or other configs)
        return authenticationManager == null || Boolean.getBoolean("app.dev-mode");
    }
}