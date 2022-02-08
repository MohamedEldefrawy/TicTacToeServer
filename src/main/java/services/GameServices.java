package services;

import model.DTOs.GameDto;
import model.DTOs.LoadGameDto;
import model.DTOs.RecordDto;
import model.DbConnection;
import model.Entities.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameServices {

    // Fields
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private String query;
    private RecordsServices recordsServices;

    // user CRUD
    public int startGame(String player1 , String player2) {
        if (connection == null)
            connection = new DbConnection().getConnection();

        query = "insert into games (playerOneName,playerTwoName) values (?,?)";

        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            this.preparedStatement = connection.prepareStatement(query);
            this.preparedStatement.setString(1, player1);
            this.preparedStatement.setString(2, player2);
            this.preparedStatement.addBatch();
            this.preparedStatement.executeBatch();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getGamesId();
    }
    public int getGamesId() {

        if (connection == null)
            connection = new DbConnection().getConnection();

        String query = "select LAST_INSERT_ID();";

        try {
            statement = connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(query);

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                System.out.println("gameID = " + id);
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public void endGame(GameDto gameDto) {
        int gameId = getGameId();
        Game currentGame = getGameById(gameId);
        currentGame.setWinner(gameDto.getWhoWins());
    }

    public  void setWinner(int gameId,int winner){
        connection = new DbConnection().getConnection();
        String query = "update games set Winner = ? where id = ?";
        try {
            this.preparedStatement = connection.prepareStatement(query);
            this.preparedStatement.setInt(1,winner);
            this.preparedStatement.setInt(2,gameId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }




   /* public void saveGame(RecordDto recordDto) {
        recordsServices = new RecordsServices();

        if (connection == null)
            connection = new DbConnection().getConnection();

        recordsServices.createRecord(recordDto);
    }*/

    public LoadGameDto loadGame(RecordDto recordDto) {
        query = "select g.id,playerOneName,playerTwoName,winner,timeStamp,requesterName,steps\n" +
                "from games  as g inner join records r \n" +
                "on g.id = r.gameId\n" +
                "where g.id = ?";

        LoadGameDto loadGameDto = new LoadGameDto();

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
                loadGameDto.setGameId(resultSet.getInt(1));
                loadGameDto.setPlayerOneName(resultSet.getString(2));
                loadGameDto.setPlayerTwoName(resultSet.getString(3));
                loadGameDto.setWinner(resultSet.getString(4));
                loadGameDto.setTimeStamp(resultSet.getString(5));
                loadGameDto.setRequesterName(resultSet.getString(6));
                loadGameDto.setSteps(resultSet.getString(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loadGameDto;
    }


    // Utilities
    public List<Game> getAllGames() {
        query = "select * from games";
        if (connection == null)
            connection = new DbConnection().getConnection();
        List<Game> games = new ArrayList<>();
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Game game = new Game();
                game.setId(resultSet.getInt(1));
                game.setPlayerOneName(resultSet.getString(2));
                game.setPlayerTwoName(resultSet.getString(3));
                game.setWinner(resultSet.getString(4));
                game.setTimeStamp(resultSet.getTimestamp(5).toLocalDateTime());
                games.add(game);
            }
            statement.close();
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public Game getGameById(int id) {
        connection = new DbConnection().getConnection();
        Game game = new Game();
        query = "select * from games where id = ?";
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            this.preparedStatement = connection.prepareStatement(query);
            this.preparedStatement.setInt(1, id);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            while (resultSet.next()) {
                game.setId(resultSet.getInt(1));
                game.setPlayerOneName(resultSet.getString(2));
                game.setPlayerTwoName(resultSet.getString(3));
                game.setWinner(resultSet.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return game;
    }

    public int getGameId() {
        query = "SELECT `AUTO_INCREMENT`\n" +
                "FROM  INFORMATION_SCHEMA.TABLES\n" +
                "WHERE TABLE_SCHEMA = 'tictactoe'\n" +
                "AND   TABLE_NAME   = 'games';";


        int gameId = 0;

        if (connection == null)
            connection = new DbConnection().getConnection();

        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            gameId = resultSet.getInt(1);
            resultSet.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gameId;
    }


    // Connection Utilities
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void saveChanges() {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
