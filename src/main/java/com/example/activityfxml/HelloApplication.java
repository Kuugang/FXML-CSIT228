package com.example.activityfxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500,500);

        scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
        Parent loginScence = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Scene loginWindow = new Scene (loginScence, 800, 600);

        stage.setScene(loginWindow);
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}