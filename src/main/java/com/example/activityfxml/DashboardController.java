package com.example.activityfxml;

import com.example.activityfxml.Database.MySQLConnection;
import com.example.activityfxml.Entities.Note;
import com.example.activityfxml.Entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardController implements Initializable{
    //TODO Check out
    //    https://stackoverflow.com/questions/21896533/javafx-2-2-onload-method-equivalent

    @FXML
    Text txtGreeting;
    @FXML
    AnchorPane paneParent;
    @FXML
    public TextArea taNote;
    @FXML
    Text txtMessage;
    @FXML
    public VBox vbxNotes;
    @FXML
    public ScrollPane spNotes;
    public static ArrayList<Note> notes;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] path = String.valueOf(url).split("/");
        String current = path[path.length - 1];

        if(Objects.equals(current, "dashboard-view.fxml")){
            txtGreeting.setText("Welcome " + User.username);
            try{
                notes = new ArrayList<>();
                MySQLConnection.getNotes();
                renderNotes();

                //TODO amobot kapoy na
//                Platform.runLater(() ->{
//                    Scene scene = paneParent.getScene();
//                    double sceneHeight = scene.getHeight();
//
//                    spNotes.setMinHeight(sceneHeight - sceneHeight / 8);
//
//                    Stage stage = (Stage) scene.getWindow();
//                    stage.heightProperty().addListener((obs, oldVal, newVal) -> {
//                        double height = scene.getHeight();
//                        System.out.println(height);
//                        spNotes.setMinHeight(height - height / 2);
//                    });
//                });
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    void renderNotes(){
        for(Note note : notes){
            if(vbxNotes.lookup("#" + note.id) != null)continue;

            HBox container = new HBox();
            container.setId(String.valueOf(note.id));
            HBox controlButtons = new HBox();
            controlButtons.setSpacing(10);

            Button btnUpdate = new Button("Update");
            Button btnDelete = new Button("Delete");

            controlButtons.getChildren().addAll(btnUpdate, btnDelete);
            controlButtons.setAlignment(Pos.BASELINE_RIGHT);
            HBox.setHgrow(controlButtons, Priority.ALWAYS);

            container.setPadding(new Insets(20));
            container.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 12px;");
            Text txtNote = new Text(note.note);

            Platform.runLater(() -> {
                txtNote.setWrappingWidth(container.getWidth() / 1.7);
                Scene scene = paneParent.getScene();
                Stage stage = (Stage) scene.getWindow();
                stage.widthProperty().addListener((obs, oldVal, newVal) -> {
                    txtNote.setWrappingWidth(container.getWidth() / 1.7);
                });
            });

            container.getChildren().add(txtNote);
            container.getChildren().add(controlButtons);

            btnDelete.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this note?", ButtonType.YES, ButtonType.NO);
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    ButtonType result = alert.getResult();
                    if (result == ButtonType.YES) {
                        try{
                            MySQLConnection.deleteNote(note.id);

                            Iterator<Note> iterator = notes.iterator();
                            while (iterator.hasNext()) {
                                Note n = iterator.next();
                                if (n.id == note.id) {
                                    iterator.remove();
                                    Scene scene = paneParent.getScene();
                                    HBox deletedNoteContainer = (HBox) scene.lookup("#" + n.id);
                                    vbxNotes.getChildren().remove(deletedNoteContainer);
                                    break;
                                }
                            }

                        }catch (SQLException e){
                            e.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, e.getMessage());
                        }
                    }
                    renderNotes();
                }
            });

            btnUpdate.setOnMouseReleased(event -> onUpdateClick(event, note, container));
            vbxNotes.getChildren().add(container);
        }
    }

    public void onHandleCreateNote(MouseEvent mouseEvent) {
        try{
            MySQLConnection.createNote(taNote.getText());
            txtMessage.setText("Noted created successfully");
        }catch (SQLException e){
            e.printStackTrace();
            txtMessage.setText(e.getMessage());
            txtMessage.setFill(Color.RED);
        }
    }

    public void onUpdateClick(MouseEvent event, Note note, HBox container) {
        container.getChildren().remove(0);
        TextField tfNewNote = new TextField();
        tfNewNote.setText(note.note);
        container.getChildren().add(0, tfNewNote);

        HBox controlButtons = (HBox) container.getChildren().get(1);
        controlButtons.getChildren().remove(0);
        HBox confirmButtons = new HBox();
        confirmButtons.setAlignment(Pos.BASELINE_RIGHT);
        HBox.setHgrow(confirmButtons, Priority.ALWAYS);
        confirmButtons.setSpacing(5);

        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");


        confirmButtons.getChildren().add(0, cancel);
        confirmButtons.getChildren().add(1, submit);
        controlButtons.getChildren().add(0, confirmButtons);

        cancel.setOnMouseReleased(event1 -> onCancelUpdateClick(event1, note, container));
        submit.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    MySQLConnection.updateNote(note, tfNewNote.getText());
                    note.note = tfNewNote.getText();

                    container.getChildren().remove(0);
                    Text txtNote = new Text(note.note);
                    container.getChildren().add(0, txtNote);
                    HBox controlButtons = (HBox) container.getChildren().get(1);
                    controlButtons.getChildren().remove(0);
                    Button btnUpdate = new Button("Update");
                    controlButtons.getChildren().add(0, btnUpdate);
                    btnUpdate.setOnMouseReleased(event1 -> onUpdateClick(event, note, container));

                    renderNotes();
                }catch (SQLException e){
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, e.getMessage());
                }
            }
        });
    }

    public void onCancelUpdateClick(MouseEvent event, Note note, HBox container){
        container.getChildren().remove(0);
        Text txtNote = new Text(note.note);
        container.getChildren().add(0, txtNote);

        HBox controlButtons = (HBox) container.getChildren().get(1);
        controlButtons.getChildren().remove(0);
        Button btnUpdate = new Button("Update");
        controlButtons.getChildren().add(0, btnUpdate);
        btnUpdate.setOnMouseReleased(event1 -> onUpdateClick(event, note, container));
    }

    public void onSignOut(MouseEvent event) throws IOException {
        MainApplication.navigate(paneParent, "login-view", "Login");
    }

    public void onNavigateCreateNote(MouseEvent event) throws IOException{
        MainApplication.navigate(paneParent, "create-note-view", "Create Note");
    }

    public void onNavigateDashboard(MouseEvent mouseEvent) throws IOException{
        MainApplication.navigate(paneParent, "dashboard-view", "Dashboard");
    }

    public void onNavigateProfile(MouseEvent event) throws IOException{
        MainApplication.navigate(paneParent, "profile-view", "Profile");
    }
}