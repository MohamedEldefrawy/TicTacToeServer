package model.Entities;

public class Game {
    public Game() {
    }

    public int getFirstPlayerId() {
        return firstPlayerId;
    }

    public void setFirstPlayerId(int firstPlayerId) {
        this.firstPlayerId = firstPlayerId;
    }

    public int getSecondPlayerId() {
        return secondPlayerId;
    }

    public void setSecondPlayerId(int secondPlayerId) {
        this.secondPlayerId = secondPlayerId;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public String getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(String gameBoard) {
        this.gameBoard = gameBoard;
    }

    private int id;
    private int firstPlayerId;
    private int secondPlayerId;
    private int winnerId;
    private String gameBoard;


    public Game(int id, int firstPlayerId, int secondPlayerId, int winnerId, String gameBoard) {
        this.id = id;
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
        this.winnerId = winnerId;
        this.gameBoard = gameBoard;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
