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
    private void clearPreviousSave(Game game) {
        if (connection == null)
            connection = new DbConnection().getConnection();
        query = "delete from usergames where playerOneId = " + game.getFirstPlayerId();

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

    public void saveGame(Game game) {
        clearPreviousSave(game);

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
            this.preparedStatement.setInt(1, game.getFirstPlayerId());
            if (game.getSecondPlayerId() != null)
                this.preparedStatement.setInt(2, game.getSecondPlayerId());
            else
                this.preparedStatement.setNull(2, Types.NULL);

            this.preparedStatement.setInt(3, game.getWinnerId());
            this.preparedStatement.setString(4, game.getGameBoard());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Game getSavedGame(int playerOneId, int playerTwoId) {
        query = "select * from usergames where playerOneId =" + playerOneId + " and playerTwoId =" + playerTwoId;

        Game game = new Game();

        if (connection == null)
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
