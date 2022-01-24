package services;

import model.DTOs.RecordDto;
import model.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class RecordsServices {

    // Fields
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private String query;

    // CRUD
    public void createRecord(RecordDto recordDto) {
        connection = new DbConnection().getConnection();
        query = "insert into records (steps,requesterName,gameId) values (?,?,?)";

        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            this.preparedStatement = connection.prepareStatement(query);
            this.preparedStatement.setString(1, recordDto.getSteps());
            this.preparedStatement.setString(2, recordDto.getRequesterName());
            this.preparedStatement.setInt(3, recordDto.getGameId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
