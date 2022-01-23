package services;


import model.DbConnection;
import model.Entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersServices {

    // Fields
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private String query;


    // user CRUD
    public void createUser() {
        connection = new DbConnection().getConnection();
        query = "insert into Users (userName,password,wins,losses,draws) values (?,?,?,?,?)";
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            this.preparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<User> getAllUsers() {
        query = "select * from users";
        if (connection == null)
            connection = new DbConnection().getConnection();
        List<User> users = new ArrayList<>();

        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4), resultSet.getInt(5),
                        resultSet.getInt(6)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public User getUserbyName(String userName) {
        return getAllUsers().stream().filter(user -> user.getUserName().equals(userName)).findFirst().get();
    }


    // Connection Utilities
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void saveChanges() throws SQLException {
        connection.commit();
    }
}
