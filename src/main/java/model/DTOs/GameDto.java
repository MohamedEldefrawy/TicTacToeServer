package model.DTOs;

public class GameDto {
    private String playerOneName;
    private int whoWins;

    public int getWhoWins() {
        return whoWins;
    }

    public void setWhoWins(int whoWins) {
        this.whoWins = whoWins;
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

    private String playerTwoName;
}
