module com.vsu.cgcourse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.vsu.cgcourse to javafx.fxml;
    exports com.vsu.cgcourse;
    exports com.vsu.cgcourse.fxml.controllers;
    opens com.vsu.cgcourse.fxml.controllers to javafx.fxml;
    exports com.vsu.cgcourse.fxml.event.handlers;
    opens com.vsu.cgcourse.fxml.event.handlers to javafx.fxml;
}