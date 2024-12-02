package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.services.AuthService;

import java.io.IOException;

public class AuthController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label feedbackText;

    @FXML
    private TextField registerUsernameField;
    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private Text registerFeedbackText;

    private final AuthService authService = new AuthService();

    @FXML
    public void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authService.authenticate(username, password)) {
            feedbackText.setText("Connexion réussie !");
            feedbackText.setStyle("-fx-fill: green;");

            // Charger l'écran principal après connexion
            loadScene("views/main.fxml", "Local Wallet");
        } else {
            feedbackText.setText("Identifiants invalides. Réessayez.");
            feedbackText.setStyle("-fx-fill: red;");
        }
    }

    @FXML
    public void logout(ActionEvent event) {
        authService.logout();
        feedbackText.setText("Logged out successfully.");
        feedbackText.setStyle("-fx-fill: blue;");
    }
    @FXML
    public void goToRegister(ActionEvent event) {
        loadScene("views/register.fxml", "S'inscrire");
    }

    /**
     * Passe à l'écran de connexion.
     */
    @FXML
    public void goToLogin(ActionEvent event) {
        loadScene("views/login.fxml", "Connexion");
    }


    @FXML
    public void register(ActionEvent event) {
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();

        if (authService.register(username, password)) {
            registerFeedbackText.setText("Inscription réussie !");
            registerFeedbackText.setStyle("-fx-fill: green;");

            // Retourner à l'écran de connexion après l'inscription
            goToLogin(event);
        } else {
            registerFeedbackText.setText("Nom d'utilisateur déjà pris.");
            registerFeedbackText.setStyle("-fx-fill: red;");
        }
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
            Stage stage = (Stage) usernameField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            feedbackText.setText("Erreur lors du chargement de l'écran.");
            feedbackText.setStyle("-fx-fill: red;");
        }
    }
}
