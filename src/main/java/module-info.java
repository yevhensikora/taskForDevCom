module com.example.taskfordevcom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;
    requires java.desktop;
    requires lombok;

    opens com.example.taskfordevcom to javafx.fxml;
    exports com.example.taskfordevcom;
    exports com.example.taskfordevcom.domain;
    opens com.example.taskfordevcom.domain to javafx.fxml;
    exports com.example.taskfordevcom.util;
    opens com.example.taskfordevcom.util to javafx.fxml;
    exports com.example.taskfordevcom.service;
    opens com.example.taskfordevcom.service to javafx.fxml;
    exports com.example.taskfordevcom.domain.piece;
    opens com.example.taskfordevcom.domain.piece to javafx.fxml;
}