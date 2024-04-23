package com.example.activityfxml;

import com.example.activityfxml.Database.MySQLConnection;
import com.example.activityfxml.Entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginRegisterController {
    public Text messageText;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfEmail;
    @FXML
    private AnchorPane paneParent;

    @FXML
    public void onRegister(){
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        String firstname = tfFirstName.getText();
        String lastname = tfLastName.getText();
        String email = tfEmail.getText();

        try{
            MySQLConnection.register(username, password, firstname, lastname, email);
            System.out.println(User.id + " " + User.firstname + " " + User.lastname + " " + User.email);
            messageText.setText("Successfully registered");
        }catch (SQLException e){
            e.printStackTrace();
            messageText.setFill(Color.RED);
            if(e.getErrorCode() == 1062){
                messageText.setText("Username already taken");
            }else{
                messageText.setText(e.getMessage());
            }
        }
    }

    @FXML
    void onSignIn(MouseEvent event) throws IOException {
        try{
            if(!(MySQLConnection.login(tfUsername.getText(), tfPassword.getText()))){
                messageText.setText("Invalid username or password");
                messageText.setFill(Color.RED);
            }else{
                MainApplication.navigate(paneParent, "dashboard-view", "Dashboard");
            }
        }catch (SQLException e){
            e.printStackTrace();
            messageText.setText(e.getMessage());
            messageText.setFill(Color.RED);
        }
    }

    public void onNavigateRegister() throws IOException {
        MainApplication.navigate(paneParent, "register-view", "Register");
    }

    public void onNavigateLogin(MouseEvent mouseEvent) throws IOException{
        MainApplication.navigate(paneParent, "login-view", "Login");
    }
}
