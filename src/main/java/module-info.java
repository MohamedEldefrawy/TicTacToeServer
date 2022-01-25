module com.server.server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.naming;
    requires java.sql;
    requires mysql.connector.java;
    requires com.google.gson;

    opens com.server to javafx.fxml;
    opens model.Entities to javafx.base;
    exports com.server;
    exports controllers;
    opens controllers to javafx.fxml;
}