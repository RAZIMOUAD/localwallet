package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.Main;
import org.example.services.AuthService;


public class AuthController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label feedbackText;


    private final AuthService authService = new AuthService();

    @FXML
    public void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authService.authenticate(username, password)) {
            feedbackText.setText("Login successful!");
            feedbackText.setStyle("-fx-fill: green;");
            Main.loadScene("/views/main.fxml"); // Load the main screen
        } else {
            feedbackText.setText("Invalid credentials. Please try again.");
            feedbackText.setStyle("-fx-fill: red;");
        }
    }


    @FXML
    public void goToRegister(ActionEvent event) {
        Main.loadScene("/views/register.fxml");
    }







}
