package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Entities.Game;
import model.Entities.Record;
import model.Entities.User;
import serverHandler.Server;
import services.GameServices;
import services.RecordsServices;
import services.UsersServices;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class ServerMenuController implements Initializable {
    //users table columns
    @FXML
    public TableView<User> users_table;
    @FXML
    public TableColumn<User, String> col_username;
    @FXML
    public TableColumn<User, String> col_password;
    @FXML
    public TableColumn<User, Integer> col_wins;
    @FXML
    public TableColumn<User, Integer> col_losses;
    @FXML
    public TableColumn<User, Integer> col_draws;
    @FXML
    public TableColumn<User, Boolean> col_online;
    //games table columns
    @FXML
    public TableView<Game> games_table;
    @FXML
    public TableColumn<Game, Integer> col_game_ID;
    @FXML
    public TableColumn<Game, String> col_player_one_name;
    @FXML
    public TableColumn<Game, String> col_player_two_name;
    @FXML
    public TableColumn<Game, String> col_winner;
    @FXML
    public TableColumn<Game, LocalDateTime> col_time;
    //records table columns
    @FXML
    public TableView<Record> records_table;
    @FXML
    public TableColumn<Record, Integer> col_record_ID;
    @FXML
    public TableColumn<Record, Integer> col_recorded_game_ID;
    @FXML
    public TableColumn<Record, String> col_requester_name;
    @FXML
    public TableColumn<Record, String> col_steps;

    public JFXButton btnStop;
    public JFXButton btnStart;
    Server server= Server.getServer();


    ObservableList<User> usersObservableList = getUsers();
    ObservableList<Game> gamesObservableList = getGames();
    ObservableList<Record> recordsObservableList = getRecords();

    public ObservableList<User> getUsers() {
        ObservableList<User> list = FXCollections.observableArrayList();
        UsersServices usersServices = new UsersServices();
        List<User> users = usersServices.getAllUsers();
        for (User user : users) {
            list.add(user);
        }
        return list;
    }

    private ObservableList<Game> getGames() {
        ObservableList<Game> list = FXCollections.observableArrayList();
        GameServices gameServices = new GameServices();
        List<Game> games = gameServices.getAllGames();
        for (Game game : games) {
            list.add(game);
        }
        return list;
    }

    private ObservableList<Record> getRecords() {
        ObservableList<Record> list = FXCollections.observableArrayList();
        RecordsServices recordsServices = new RecordsServices();
        List<Record> records = recordsServices.getAllRecords();
        for (Record record : records) {
            list.add(record);
        }
        return list;
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initializing usersTable
        col_username.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        col_password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        col_wins.setCellValueFactory(new PropertyValueFactory<User, Integer>("wins"));
        col_losses.setCellValueFactory(new PropertyValueFactory<User, Integer>("losses"));
        col_draws.setCellValueFactory(new PropertyValueFactory<User, Integer>("draws"));
        col_online.setCellValueFactory(new PropertyValueFactory<User, Boolean>("status"));
        users_table.setItems(usersObservableList);

        //initializing gamesTable
        col_game_ID.setCellValueFactory(new PropertyValueFactory<Game, Integer>("id"));
        col_player_one_name.setCellValueFactory(new PropertyValueFactory<Game, String>("playerOneName"));
        col_player_two_name.setCellValueFactory(new PropertyValueFactory<Game, String>("playerTwoName"));
        col_winner.setCellValueFactory(new PropertyValueFactory<Game, String>("Winner"));
        col_time.setCellValueFactory(new PropertyValueFactory<Game, LocalDateTime>("timeStamp"));
        games_table.setItems(gamesObservableList);

        //initializing gamesTable
        col_record_ID.setCellValueFactory(new PropertyValueFactory<Record, Integer>("id"));
        col_steps.setCellValueFactory(new PropertyValueFactory<Record, String>("steps"));
        col_requester_name.setCellValueFactory(new PropertyValueFactory<Record, String>("requesterName"));
        col_recorded_game_ID.setCellValueFactory(new PropertyValueFactory<Record, Integer>("gameId"));
        records_table.setItems(recordsObservableList);

        btnStart.setOnAction(actionEvent -> {server.startServerHandlerThread();});
        btnStop.setOnAction(actionEvent -> {server.stopServerHandlerThread();});
    }

}
