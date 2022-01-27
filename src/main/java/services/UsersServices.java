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
    public void createUser(String username, String password,int wins,int losses,int draws) {
        connection = new DbConnection().getConnection();
        query = "insert into Users (userName,password,wins,losses,draws) values (?,?,?,?,?)";
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            this.preparedStatement = connection.prepareStatement(query);
            this.preparedStatement.setString(1,username );
            this.preparedStatement.setString(2, password);
            this.preparedStatement.setInt(3,wins);
            this.preparedStatement.setInt(4,losses);
            this.preparedStatement.setInt(5, draws);
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
        return getAllUsers().stream().filter(user -> user.getUserName().equalsIgnoreCase(userName)).findFirst().get();
    }

    public void updateUser(User user) {
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

    public boolean login(String username,String password) {
        boolean result = false;
        User selectedUser = getUserByName(username);
        if (selectedUser != null && selectedUser.getPassword().equals(password)) {
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
