package com.grpcstreamings.resturantapp.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class SceneUtils {
    public static void changeRoot(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(SceneUtils.class.getResource(fxmlPath))
        );
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);

        try {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            SceneUtils.class.getResource("/styles.css")
                    ).toExternalForm()
            );
        } catch (NullPointerException e) {
            System.out.println("Stylesheet not found");
        }
    }
}