package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Entities.Game;
import services.UserGameService;

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
//        UsersServices services = new UsersServices();
        UserGameService gameService = new UserGameService();
        Game game = new Game();
//        services.createUser();
//        PreparedStatement preparedStatement = services.getPreparedStatement();

        gameService.saveGame();
        game.setFirstPlayerId(5);
        game.setSecondPlayerId(6);
        game.setWinnerId(5);
        game.setGameBoard("123456789");
        PreparedStatement preparedStatement = gameService.getPreparedStatement();

        try {
            preparedStatement.setInt(1, game.getFirstPlayerId());
            preparedStatement.setInt(2, game.getSecondPlayerId());
            preparedStatement.setInt(3, game.getWinnerId());
            preparedStatement.setString(4, game.getGameBoard());
            preparedStatement.addBatch();
            int[] count = preparedStatement.executeBatch();
            gameService.saveChanges();
            System.out.println(count.length + " rows changed");

            preparedStatement.close();
            gameService.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }


//        User user = new User();

//        user.setUserName("hash");
//        user.setPassword("123");
//        user.setWins(1);
//        user.setLosses(1);
//        user.setDraws(1);

//        try {
//            preparedStatement.setString(1, user.getUserName());
//            preparedStatement.setString(2, user.getPassword());
//            preparedStatement.setInt(3, user.getWins());
//            preparedStatement.setInt(4, user.getLosses());
//            preparedStatement.setInt(5, user.getDraws());
//            preparedStatement.addBatch();
//            int[] count = preparedStatement.executeBatch();
//
//            services.saveChanges();
//            preparedStatement.close();
//
//            List<User> users = services.getAllUsers();
//            System.out.println(count.length + " rows changed");
//            users.stream().forEach(user1 -> System.out.println(user1.getUserName()));
//
//            User selectedUser = services.getUserbyName("dafro");
//
//            System.out.println(selectedUser.getUserName() + " Wins = " + selectedUser.getWins());
//
//
//            services.closeConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        welcomeText.setText("Welcome to JavaFX Application!");


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnWelcome.setOnAction(actionEvent -> onHelloButtonClick());
    }
}