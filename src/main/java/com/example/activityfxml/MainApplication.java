package com.example.activityfxml;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent loginScene = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Scene loginWindow = new Scene (loginScene, 800, 600);
        loginWindow.getStylesheets().add(getClass().getResource("index.css").toExternalForm());
        stage.setScene(loginWindow);
        stage.setTitle("Login");
        stage.show();
    }

    public static void navigate(AnchorPane paneParent, String FXMLLayout, String windowTitle) throws IOException {
        AnchorPane root = (AnchorPane) paneParent.getParent();
        Parent registerView = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource(FXMLLayout + ".fxml")));
        Scene scene = root.getScene();
        scene.getStylesheets().add(MainApplication.class.getResource("index.css").toExternalForm());

        root.getChildren().clear();
        root.getChildren().add(registerView.getChildrenUnmodifiable().get(0));

        Stage stage = (Stage) scene.getWindow();
        stage.setTitle(windowTitle);
    }
    public static void main(String[] args) {
        launch();
    }
}