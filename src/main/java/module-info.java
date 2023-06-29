module com.example.taskfordevcom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;
    requires java.desktop;

    opens com.example.taskfordevcom to javafx.fxml;
    exports com.example.taskfordevcom;
}