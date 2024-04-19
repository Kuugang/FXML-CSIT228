module com.example.activityfxml {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.activityfxml to javafx.fxml;
    exports com.example.activityfxml;
}