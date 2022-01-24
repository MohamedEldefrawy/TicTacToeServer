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
    public void createUser(User newUser) {
        connection = new DbConnection().getConnection();
        query = "insert into Users (userName,password,wins,losses,draws) values (?,?,?,?,?)";
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            this.preparedStatement = connection.prepareStatement(query);
            this.preparedStatement.setString(1, newUser.getUserName());
            this.preparedStatement.setString(2, newUser.getPassword());
            this.preparedStatement.setInt(3, newUser.getWins());
            this.preparedStatement.setInt(4, newUser.getLosses());
            this.preparedStatement.setInt(5, newUser.getDraws());
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
                users.add(new User(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        resultSet.getInt(6)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserbyName(String userName) {
        return getAllUsers().stream().filter(user -> user.getUserName().equalsIgnoreCase(userName)).findFirst().get();
    }

    public void updateUser(User user) {
        User selectedUser = getUserbyName(user.getUserName());
        selectedUser.setWins(user.getWins());
        selectedUser.setLosses(user.getLosses());
        selectedUser.setDraws(user.getDraws());

        connection = new DbConnection().getConnection();
        query = "update Users set wins = ? , losses = ? , draws = ?  where userName = ?";
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            this.preparedStatement = connection.prepareStatement(query);
            this.preparedStatement.setInt(1, user.getWins());
            this.preparedStatement.setInt(2, user.getLosses());
            this.preparedStatement.setInt(3, user.getDraws());
            this.preparedStatement.setString(4, user.getUserName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStatus(User user, boolean isLoggedIn) {
        query = "update users set isLoggedIn = " + isLoggedIn + " where userName = " + "'" + user.getUserName() + "'";

        if (connection == null)
            connection = new DbConnection().getConnection();

        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            statement.execute(query);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean login(User user) {
        boolean result = false;
        User selectedUser = getUserbyName(user.getUserName());
        if (selectedUser != null && selectedUser.getPassword().equals(user.getPassword())) {
            updateStatus(selectedUser, true);
            result = true;
        }
        return result;
    }

    public void logout(User user) {
        updateStatus(user, false);
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
