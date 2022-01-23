module com.server.server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.naming;
    requires java.sql;
    requires mysql.connector.java;

    opens com.server to javafx.fxml;
    exports com.server;
    exports controllers;
    opens controllers to javafx.fxml;
}