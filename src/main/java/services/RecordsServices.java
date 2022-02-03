package services;

import model.DTOs.RecordDto;
import model.DbConnection;
import model.Entities.Record;
import model.Entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordsServices {

    // Fields
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private String query;

    // CRUD
    public void createRecord(String moves,String player1,int gameId) {
        connection = new DbConnection().getConnection();
        query = "insert into records (steps,requesterName,gameId) values (?,?,?)";

        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            this.preparedStatement = connection.prepareStatement(query);
            this.preparedStatement.setString(1, moves);
            this.preparedStatement.setString(2, player1);
            this.preparedStatement.setInt(3, gameId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Utilities
    public List<Record> getAllRecords() {
        query = "select * from records";
        if (connection == null)
            connection = new DbConnection().getConnection();
        List<Record> records = new ArrayList<>();
        try {
            statement = connection.createStatement();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
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
