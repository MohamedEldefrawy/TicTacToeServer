package model.DTOs;

public class GameFinishedDto {
    String winner;
    int finishedGameId;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getFinishedGameId() {
        return finishedGameId;
    }

    public void setFinishedGameId(int finishedGameId) {
        this.finishedGameId = finishedGameId;
    }

}
