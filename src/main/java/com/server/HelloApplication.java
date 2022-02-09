package com.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage currentStage;
    public static Stage getStage() {
        return currentStage;
    }
    @Override
    public void start(Stage stage) throws IOException {
        currentStage=stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/server/views/serverMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}