package services;

import model.DbConnection;
import model.Entities.Record;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordsServices {


    // CRUD
    public void createRecord(String moves, String player1, String player2, int gameId) {
        try (Connection connection = new DbConnection().getConnection()) {
            String firstPlayerQuery = "insert into records (steps,requesterName,gameId) values (?,?,?)";
            String secondPlayerQuery = "insert into records (steps,requesterName,gameId) values (?,?,?)";

            var firstPlayerPreparedStatement = connection.prepareStatement(firstPlayerQuery);
            firstPlayerPreparedStatement.setString(1, moves);
            firstPlayerPreparedStatement.setString(2, player1);
            firstPlayerPreparedStatement.setInt(3, gameId);
            firstPlayerPreparedStatement.addBatch();
            firstPlayerPreparedStatement.executeBatch();
            firstPlayerPreparedStatement.close();

            var secondPreparedStatement = connection.prepareStatement(secondPlayerQuery);
            secondPreparedStatement.setString(1, moves);
            secondPreparedStatement.setString(2, player2);
            secondPreparedStatement.setInt(3, gameId);
            secondPreparedStatement.addBatch();
            secondPreparedStatement.executeBatch();
            secondPreparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Utilities
    public List<Record> getAllRecords() {
        try (Connection connection = new DbConnection().getConnection()) {
            String query = "select * from records";
            List<Record> records = new ArrayList<>();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Record record = new Record();
                record.setId(resultSet.getInt(1));
                record.setSteps(resultSet.getString(2));
                record.setRequesterName(resultSet.getString(3));
                record.setGameId(resultSet.getInt(4));
                records.add(record);
            }
            statement.close();
            resultSet.close();
            connection.close();
            return records;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
