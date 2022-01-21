package com.server.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/server/server/views/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        scene.getStylesheets().add(getClass().getResource("/com/server/server/css/style.css").toString());
//        System.out.println(getClass().getResource("/com/server/server/css/style.css"));
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}