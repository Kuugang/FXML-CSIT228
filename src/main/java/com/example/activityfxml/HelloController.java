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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class HelloController {

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
        users.put("test", "test");
        users.put("test2", "test2");
        users.put("test3", "test3");

        if(users.containsKey(inputUsername.getText())&& users.get(inputUsername.getText()).equals(inputPassword.getText())){
            user = inputUsername.getText();
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard-view.fxml")));
            paneParent.getScene().getStylesheets().clear();
            paneParent.getChildren().clear();
            paneParent.getChildren().add(scene);

            if(Objects.equals(inputUsername.getText(), "test")){
                paneParent.getScene().getStylesheets().add(getClass().getResource("user1.css").toExternalForm());
            }else if(Objects.equals(inputUsername.getText(), "test2")){
                paneParent.getScene().getStylesheets().add(getClass().getResource("user2.css").toExternalForm());
            }else if(Objects.equals(inputUsername.getText(), "test3")){
                paneParent.getScene().getStylesheets().add(getClass().getResource("user3.css").toExternalForm());
            }

        }else{
            messageText.setFill(Color.RED);
            messageText.setText("username or password does not exist");
        }
    }

    public void onSignOut(ActionEvent event) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("\n.button{\n\t-fx-background-color: #");

        String cpColor = cpPicker.getValue().toString();
        sb.append(cpColor.substring(2, cpColor.length() - 2));
        sb.append(";\n}");
        System.out.println(cpPicker.getValue());

        try{
            BufferedWriter bw = null;
            if(Objects.equals(user, "test")){
               bw = new BufferedWriter(new FileWriter(getClass().getResource("user1.css").getPath(), true));
            }else  if (Objects.equals(user, "test2")) {
                bw = new BufferedWriter(new FileWriter(getClass().getResource("user2.css").getPath(), true));
            }else if(Objects.equals(user, "test3")){
                bw = new BufferedWriter(new FileWriter(getClass().getResource("user3.css").getPath(), true));
            }
            bw.write(sb.toString());
            bw.close();
        }catch (IOException e){

        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Main Screen");
        stage.show();
    }
}
