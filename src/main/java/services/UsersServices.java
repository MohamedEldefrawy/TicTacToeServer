package services;


import model.DbConnection;
import model.Entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class UsersServices {

    // Fields
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private String query;


    // user CRUD
    public boolean createUser(String username, String password, int wins, int losses, int draws) {
        connection = new DbConnection().getConnection();
        query = "insert into users (userName,password,wins,losses,draws) values (?,?,?,?,?)";
        int rowsAffected = 0;
        Boolean result = false;
        try {
            this.preparedStatement = connection.prepareStatement(query);
            this.preparedStatement.setString(1, username);
            this.preparedStatement.setString(2, password);
            this.preparedStatement.setInt(3, wins);
            this.preparedStatement.setInt(4, losses);
            this.preparedStatement.setInt(5, draws);
            this.preparedStatement.addBatch();
            rowsAffected = this.preparedStatement.executeBatch().length;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowsAffected > 0;
    }

    public List<User> getAllUsers() {
        query = "select * from users";
        if (connection == null)
            connection = new DbConnection().getConnection();
        List<User> users = new ArrayList<>();

        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User user = new User();
                user.setUserName(resultSet.getString(1));
                user.setPassword(resultSet.getString(2));
                user.setWins(resultSet.getInt(3));
                user.setLosses(resultSet.getInt(4));
                user.setDraws(resultSet.getInt(5));
                user.setStatus(resultSet.getBoolean(6));

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public User getUserByName(String userName) {
        return getAllUsers().stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst().get();
    }

    public void updateUser(User user) {
        connection = new DbConnection().getConnection();

        query = "update users set wins = ? , losses = ? , draws = ?  where userName = ?";
        try {
            this.preparedStatement = connection.prepareStatement(query);
            this.preparedStatement.setInt(1, user.getWins());
            this.preparedStatement.setInt(2, user.getLosses());
            this.preparedStatement.setInt(3, user.getDraws());
            this.preparedStatement.setString(4, user.getUserName());
            this.preparedStatement.addBatch();
            this.preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(User user, boolean isLoggedIn) {
        query = "update users set isLoggedIn = "
                + isLoggedIn + " where userName = " + "'" + user.getUserName() + "'";
        if (connection == null)
            connection = new DbConnection().getConnection();

        try {
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkValidation(String username) {
        try {
            User user = getUserByName(username);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public boolean login(String username, String password) {
        try {
            User selectedUser = getUserByName(username);
            if (selectedUser != null && selectedUser.getPassword().equals(password)) {
                updateStatus(selectedUser, true);
                return true;
            } else
                return false;

        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public List<User> getAllOnlineUsers() {
        List<User> onlineUsers = new ArrayList<User>();
        String query = "select * from users where isLoggedIn = true";
        if (connection == null)
            connection = new DbConnection().getConnection();
        try {
            statement = connection.createStatement();
            ResultSet var = this.statement.executeQuery(query);
            while (var.next()) {
                User user = new User(var.getString(1), var.getString(2), var.getInt(3), var.getInt(4), var.getInt(5));
                onlineUsers.add(user);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return onlineUsers;
    }

    public void logout(User user) {
        updateStatus(user, false);
    }

    // Connection Utilities
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveChanges() {
        try {
            connection.setAutoCommit(false);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
