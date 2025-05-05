package com.grpcstreamings.resturantapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        InputStream iconStream = getClass().getResourceAsStream("/icons/icon.png");
        if (iconStream != null) {
            Image icon = new Image(iconStream);
            stage.getIcons().add(icon);
        } else {
            System.err.println("Icon not found! Check file path");
        }

        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource(
                        "/com/grpcstreamings/resturantapp/fxml/login.fxml")
                ));
        Scene scene = new Scene(root);

        // Apply global CSS
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}