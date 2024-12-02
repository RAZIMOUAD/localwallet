package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        loadScene("/views/login.fxml"); // Initialiser avec l'écran de connexion
        primaryStage.setTitle("LocalWallet");
        primaryStage.show();
    }

    public static void loadScene(String fxmlFilePath) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFilePath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la scène : " + fxmlFilePath);
        }
    }
   
    public static void main(String[] args) {
        launch(args);
    }
}
