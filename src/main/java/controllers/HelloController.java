package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.DTOs.LoginUserDto;
import model.Utilities.JsonBuilder;
import services.GameServices;
import services.RecordsServices;
import services.UsersServices;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Label welcomeText;

    @FXML
    private Button btnWelcome;

    @FXML
    protected void onHelloButtonClick() {

        welcomeText.setText("Welcome to JavaFX Application!");
        UsersServices usersServices = new UsersServices();
        GameServices gameServices = new GameServices();
        RecordsServices recordsServices = new RecordsServices();

        usersServices.getAllUsers().stream().forEach(user -> System.out.println(user.getUserName()));

        // Create user


      /*  User newUser = new User();
        newUser.setUserName("MO");
        newUser.setPassword("1234");
        usersServices.createUser(newUser);
        try {
            usersServices.getPreparedStatement().execute();
            usersServices.saveChanges();

            usersServices.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/


        // Update user wins, loss and draws

        /* User newUser = new User();
        newUser.setUserName("Dafro");
        newUser.setWins(1);
        usersServices.updateUser(newUser);
        try {
            usersServices.getPreparedStatement().execute();
            usersServices.saveChanges();
            usersServices.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        /*List<User> userList = usersServices.getAllUsers();

        GameDto gameDto = new GameDto();
        gameDto.setPlayerOneName(userList.get(0).getUserName());
        gameDto.setPlayerTwoName(userList.get(1).getUserName());
        gameServices.startGame(gameDto);
        try {
            gameServices.getPreparedStatement().execute();
            gameServices.saveChanges();

            gameServices.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

      /*  User newUser = new User();
        newUser.setUserName("DAFRO");
        newUser.setWins(1);
        RecordDto recordDto = new RecordDto();
        recordDto.setRequesterName(newUser.getUserName());

        recordDto.setGameId(gameServices.getGameId());
        recordDto.setSteps("123567854");
        recordsServices.createRecord(recordDto);

        try {
            recordsServices.getPreparedStatement().execute();
            recordsServices.saveChanges();
        } catch (SQLException e) {
            e.printStackTrace();
        }
*/
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnWelcome.setOnAction(actionEvent -> onHelloButtonClick());
    }
}