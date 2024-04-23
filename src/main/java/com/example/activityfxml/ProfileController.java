package com.example.activityfxml;

import com.example.activityfxml.Database.MySQLConnection;
import com.example.activityfxml.Entities.Note;
import com.example.activityfxml.Entities.User;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    public AnchorPane paneParent;
    public Text firstName;
    public Text lastName;
    public Text username;
    public Text email;
    public VBox profile;
    public HBox editProfileControls;
    public Text txtMessage;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstName.setText(User.firstname);
        lastName.setText(User.lastname);
        username.setText(User.username);
        email.setText(User.email);
    }

    public void handleOnEditProfile(MouseEvent event) {
        HBox firstNameContainer = (HBox) firstName.getParent();
        HBox lastNameContainer = (HBox)lastName.getParent();
        HBox usernameContainer = (HBox)username.getParent();
        HBox emailContainer = (HBox)email.getParent();

        TextField tfFirstname = new TextField(User.firstname);
        TextField tFLastname = new TextField(User.lastname);
        TextField tFUsername = new TextField(User.username);
        TextField tFEmail = new TextField(User.email);

        firstNameContainer.getChildren().add(tfFirstname);
        firstNameContainer.getChildren().remove(1);

        lastNameContainer.getChildren().add(tFLastname);
        lastNameContainer.getChildren().remove(1);

        usernameContainer.getChildren().add(tFUsername);
        usernameContainer.getChildren().remove(1);


        emailContainer.getChildren().add(tFEmail);
        emailContainer.getChildren().remove(1);

        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");

        submit.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    if(MySQLConnection.updateProfile(tfFirstname.getText(), tFLastname.getText(), tFUsername.getText(), tFEmail.getText()) > 0){
                        txtMessage.setText("Successfully updated profile");
                        txtMessage.setFill(Color.GREEN);
                        User.firstname = tfFirstname.getText();
                        User.lastname = tFLastname.getText();
                        User.username = tFUsername.getText();
                        User.email = tFEmail.getText();
                    }else{
                        txtMessage.setText("No fields to update");
                        txtMessage.setFill(Color.RED);
                    };
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR , e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        cancel.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                AnchorPane anchorPane = (AnchorPane) paneParent.getParent();
                try {
                    anchorPane.getChildren().setAll((Parent) FXMLLoader.load(Objects.requireNonNull(getClass().getResource("profile-view.fxml"))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene scene = anchorPane.getScene();
                Stage stage = (Stage) scene.getWindow();
                stage.setTitle("Profile");
            }
        });

        editProfileControls.getChildren().remove(0);
        editProfileControls.getChildren().add(0, submit);
        editProfileControls.getChildren().add(1, cancel);
    }

    public void handleOnDeleteAccount(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete your account?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.showAndWait();
        ButtonType result = alert.getResult();

        if (result == ButtonType.YES) {
            try{
                MySQLConnection.deleteAccount();
                MainApplication.navigate(paneParent, "login-view", "Login");
            }catch (SQLException e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage());
            }
        }

    }

    public void onNavigateDashboard(MouseEvent event) throws IOException {
        MainApplication.navigate(paneParent, "dashboard-view", "Dashboard");
    }

    public void onNavigateCreateNote(MouseEvent event) throws IOException {
        MainApplication.navigate(paneParent, "create-note-view", "Create Note");
    }

    public void onSignOut(MouseEvent event) throws IOException {
        MainApplication.navigate(paneParent, "login-view", "Login");
    }

    public void onNavigateProfile(MouseEvent event) throws IOException {
        MainApplication.navigate(paneParent, "profile-view", "Profile");
    }
}