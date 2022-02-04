module com.server.server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.naming;
    requires java.sql;
    requires mysql.connector.java;
    requires com.google.gson;
    requires com.jfoenix;

    opens com.server to javafx.fxml;
    opens model.Entities to javafx.base;
    opens model.DTOs to com.google.gson;
    exports com.server;
    exports controllers;
    exports model.DTOs to com.google.gson;
    opens controllers to javafx.fxml;
}