package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.example.Main;
import org.example.services.AuthService;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label feedbackText;

    private final AuthService authService = new AuthService();

    @FXML
    public void register(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            feedbackText.setText("Les mots de passe ne correspondent pas.");
            feedbackText.setStyle("-fx-fill: red;");
            return;
        }

        if (authService.register(username, password)) {
            feedbackText.setText("Inscription réussie !");
            feedbackText.setStyle("-fx-fill: green;");
            Main.loadScene("/views/login.fxml"); // Redirection vers la connexion
        } else {
            feedbackText.setText("Nom d'utilisateur déjà pris.");
            feedbackText.setStyle("-fx-fill: red;");
        }
    }

    @FXML
    public void backToLogin(ActionEvent event) {
        Main.loadScene("/views/login.fxml"); // Retour à la connexion
    }
}
