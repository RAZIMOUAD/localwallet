package org.example.controllers;

import org.example.services.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Text feedbackText;

    private final AuthService authService = new AuthService();

    @FXML
    public void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            feedbackText.setText("Veuillez remplir tous les champs.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            feedbackText.setText("Les mots de passe ne correspondent pas.");
            return;
        }

        if (authService.register(username, password)) {
            feedbackText.setStyle("-fx-fill: green;");
            feedbackText.setText("Enregistrement réussi ! Retournez à la connexion.");
        } else {
            feedbackText.setText("Nom d'utilisateur déjà pris.");
        }
    }

    @FXML
    public void handleBackToLogin(ActionEvent event) {
        // Redirection vers l'écran de connexion
        System.out.println("Redirection vers la connexion.");
    }
}
