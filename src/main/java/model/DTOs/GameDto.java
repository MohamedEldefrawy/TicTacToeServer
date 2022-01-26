package model.DTOs;

public class GameDto {
    private String playerOneName;
    private String whoWins;

    public String getWhoWins() {
        return whoWins;
    }

    public void setWhoWins(String whoWins) {
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
