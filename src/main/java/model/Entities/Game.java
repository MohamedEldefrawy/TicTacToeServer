package model.Entities;

import java.time.LocalDateTime;

public class Game {
    public Game() {
    }
    private int id;
    private String playerOneName;
    private String playerTwoName;
    private int winner;    //   1 , 2 ,  3     Winner
    private LocalDateTime timeStamp;


    public Game(int id, String playerOneName, String playerTwoName, Integer winner) {
        this.id = id;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.winner = winner;
        this.timeStamp = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
