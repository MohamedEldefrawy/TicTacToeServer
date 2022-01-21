module com.server.server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.server to javafx.fxml;
    exports com.server;
    exports controllers;
    opens controllers to javafx.fxml;
}