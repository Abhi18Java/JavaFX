package com.grpcstreamings.resturantapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the login screen first
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource(
                        "/com/grpcstreamings/resturantapp/fxml/login.fxml")
                ));
        Scene scene = new Scene(root);

        // Apply global CSS
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );

        stage.setTitle("Restaurant App Login");
        stage.setScene(scene);
        stage.setResizable(false); // Optional: Prevent resizing
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}