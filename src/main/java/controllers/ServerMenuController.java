package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Entities.User;
import services.UsersServices;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ServerMenuController implements Initializable {

    @FXML
    public TableView<User> usersTable;
    @FXML
    public TableColumn<User, String> col_userName;
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

    ObservableList<User> userObservableList = getUsers();

    public ObservableList<User> getUsers() {
        ObservableList<User> list = FXCollections.observableArrayList();
        UsersServices usersServices = new UsersServices();
        List<User> users = usersServices.getAllUsers();

        for (User user : users) {
            list.add(user);
        }

        list.stream().forEach(user -> System.out.println(user.getStatus()));

        return list;
    }


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_userName.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        col_password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        col_wins.setCellValueFactory(new PropertyValueFactory<User, Integer>("wins"));
        col_losses.setCellValueFactory(new PropertyValueFactory<User, Integer>("losses"));
        col_draws.setCellValueFactory(new PropertyValueFactory<User, Integer>("draws"));
        col_online.setCellValueFactory(new PropertyValueFactory<User, Boolean>("status"));
        usersTable.setItems(getUsers());
    }

}
