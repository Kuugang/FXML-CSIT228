package com.example.activityfxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField inputUsername;
    @FXML
    private TextField inputPassword;
    @FXML
    private TextField inputFirstName;
    @FXML
    private TextField inputLastName;
    @FXML
    private TextField inputEmail;

    @FXML
    private Text txtResultText;

    @FXML
    public void onRegister(ActionEvent event){
        String username = inputUsername.getText();
        String password = inputPassword.getText();
        String firstname = inputFirstName.getText();
        String lastname = inputLastName.getText();
        String email = inputEmail.getText();

        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO users (username, password, firstname, lastname, email) VALUES (?, ?, ?, ?, ?)"
            )){

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, firstname);
            statement.setString(4,lastname);
            statement.setString(5, email);

            statement.executeUpdate();

            txtResultText.setText("Successfully registered");
        }catch (SQLException e){
            e.printStackTrace();
            txtResultText.setFill(Color.RED);
            if(e.getErrorCode() == 1062){
                txtResultText.setText("Username already taken");
            }else{
                txtResultText.setText("Something went wrong");
            }
        }
    }
    public void onLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }
}
