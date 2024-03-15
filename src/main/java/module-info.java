module com.example.activityfxml {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.activityfxml to javafx.fxml;
    exports com.example.activityfxml;
}