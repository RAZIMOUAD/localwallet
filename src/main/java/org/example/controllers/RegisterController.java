package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Main;
import org.example.services.AuthService;
import org.example.services.WalletService;
import org.example.utils.SessionManager;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField dateOfBirthField;

    @FXML
    private TextField securityQuestionField;

    @FXML
    private TextField securityAnswerField;

    @FXML
    private Label feedbackText;

    private final AuthService authService = new AuthService();

    @FXML
    public void register(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String country = countryField.getText();
        String dateOfBirth = dateOfBirthField.getText();
        String securityQuestion = securityQuestionField.getText();
        String securityAnswer = securityAnswerField.getText();


        if (!password.equals(confirmPassword)) {
            feedbackText.setText("Passwords do not match.");
            feedbackText.setStyle("-fx-fill: red;");
            return;
        }
        // Register the user
        if (authService.register(username, password, email, phoneNumber, country, dateOfBirth, securityQuestion, securityAnswer)) {
            // Retrieve the user ID
            int userId = authService.getUserIdByUsername(username);
            if (userId > 0) {
                // Save the user ID in the session
                SessionManager.setLoggedInUserId(userId);

                feedbackText.setText("Registration successful!");
                feedbackText.setStyle("-fx-fill: green;");
                // Redirect to dashboard.fxml
                redirectToDashboard();
            } else {
                feedbackText.setText("An error occurred while retrieving user details.");
                feedbackText.setStyle("-fx-fill: red;");
            }
        } else {
            feedbackText.setText("Username is already taken or an error occurred.");
            feedbackText.setStyle("-fx-fill: red;");
        }
    }

    @FXML
    public void backToLogin(ActionEvent event) {
        try {
            // Load the login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source (button)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene for the login page
            stage.setScene(new Scene(root));
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading the login page: " + e.getMessage());
        }
    }


    private void redirectToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(loader.load());

            // Pass WalletService to the WalletController
            WalletController walletController = loader.getController();
            walletController.setWalletService(new WalletService());

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            feedbackText.setText("Error loading dashboard.");
            feedbackText.setStyle("-fx-fill: red;");
        }
    }
}
