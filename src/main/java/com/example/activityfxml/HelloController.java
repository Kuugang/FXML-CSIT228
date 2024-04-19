package com.example.activityfxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

public class HelloController {
    public static int userid;
    public static String firstname;
    public static String lastname;
    public static String email;
    public static String username;

    public ColorPicker cpPicker;
    public Text messageText;

    @FXML
    private TextField inputPassword;

    @FXML
    private TextField inputUsername;
    @FXML
    private Pane paneParent;
    public static String user;
    HashMap<String, String> users = new HashMap<>();
    @FXML
    void onSignIn(ActionEvent event) throws IOException {
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
            );
            ){
            statement.setString(1, inputUsername.getText());
            statement.setString(2, inputPassword.getText());

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                userid = resultSet.getInt("id");
                firstname = resultSet.getString("firstname");
                lastname = resultSet.getString("lastname");
                email = resultSet.getString("email");
                username = resultSet.getString("username");

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
                Parent root = (Parent) fxmlLoader.load();

//                VBox vbox = (VBox) root.getChildrenUnmodifiable().get(0);
//                Text greeting = (Text)vbox.getChildren().get(0);
//                greeting.setText(greeting.getText() + " " + username);
//
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setScene(scene);
                stage.setTitle("Dashboard");
                stage.show();
            }else{
                messageText.setText("Invalid username or password");
                messageText.setFill(Color.RED);
                System.out.println("Invalid username or password");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void onRegister(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register-view.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.show();
    }
}
