package model.Entities;


public class User {
    private int id;

    private String userName;

    private String password;

    private int wins;

    private int losses;

    private int draws;

    public User(String userName, String password, int wins, int losses, int draws) {
        this.userName = userName;
        this.password = password;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

}
