package services;

import model.DbConnection;
import model.Entities.Game;

import java.sql.*;

public class UserGameService {

    // Fields
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private String query;

    // user CRUD
    public void saveGame() {
        if (connection == null)
            connection = new DbConnection().getConnection();
        query = "insert into usergames (playerOneId,playerTwoId,WinnerId,gameBoard) values (?,?,?,?)";

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

    public Game getSavedGame(int playerOneId, int playerTwoId) {
        query = "select * from usergames where playerOneId =" + playerOneId + " and playerTwoId =" + playerTwoId;

        Game game = new Game();

        connection = new DbConnection().getConnection();

        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                game.setId(resultSet.getInt(1));
                game.setFirstPlayerId(resultSet.getInt(2));
                game.setSecondPlayerId(resultSet.getInt(3));
                game.setWinnerId(resultSet.getInt(4));
                game.setGameBoard(resultSet.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Winner Id" + game.getFirstPlayerId());
        return game;
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
