package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.DbConnection;
import model.Entities.User;
import services.GameServices;
import services.RecordsServices;
import services.UsersServices;

import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ServerMenuController implements Initializable {

    @FXML
    public TableView<User> usersTable;
    @FXML
    public TableColumn<User,String> col_userName;
    @FXML
    public TableColumn<User,String> col_password;
    @FXML
    public TableColumn<User,Integer> col_wins;
    @FXML
    public TableColumn<User,Integer> col_losses;
    @FXML
    public TableColumn<User,Integer> col_draws;
    @FXML
    public TableColumn<User,Integer> col_online;

    ObservableList<User> userObservableList = getDatausers();

    public static ObservableList<User> getDatausers(){
        Connection conn = new DbConnection().getConnection();
        ObservableList<User> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("select * from users");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                list.add(new User(rs.getString("userName"), rs.getString("password"), rs.getInt("wins"), rs.getInt("losses"), rs.getInt("draws")));
            }
        } catch (Exception e) {
        }
        return list;
    }


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        col_userName.setCellValueFactory(new PropertyValueFactory<User,String>("userName"));
        col_password.setCellValueFactory(new PropertyValueFactory<User,String>("password"));
        col_wins.setCellValueFactory(new PropertyValueFactory<User,Integer>("wins"));
        col_losses.setCellValueFactory(new PropertyValueFactory<User,Integer>("losses"));
        col_draws.setCellValueFactory(new PropertyValueFactory<User,Integer>("draws"));
        col_online.setCellValueFactory(new PropertyValueFactory<User,Integer>("isLoggedIn"));

        usersTable.setItems(getDatausers());
    }

}
