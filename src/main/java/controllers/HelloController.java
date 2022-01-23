package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Entities.User;
import services.UsersServices;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Label welcomeText;

    @FXML
    private Button btnWelcome;

    @FXML
    protected void onHelloButtonClick() {
        UsersServices services = new UsersServices();
        services.createUser();
        PreparedStatement preparedStatement = services.getPreparedStatement();

        User user = new User();

        user.setUserName("jajajaja");
        user.setPassword("123");
        user.setWins(1);
        user.setLosses(1);
        user.setDraws(1);
        try {
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getWins());
            preparedStatement.setInt(4, user.getLosses());
            preparedStatement.setInt(5, user.getDraws());
            preparedStatement.addBatch();
            int[] count = preparedStatement.executeBatch();

            services.saveChanges();
            preparedStatement.close();
            services.closeConnection();
            System.out.println(count.length + " rows changed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnWelcome.setOnAction(actionEvent -> onHelloButtonClick());
    }
}