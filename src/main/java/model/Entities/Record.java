package model.Entities;

public class Record {
    private int id;
    private String steps;
    private String requesterName;
    private int gameId;

    public Record(String steps, String requesterName, int gameId) {
        this.steps = steps;
        this.requesterName = requesterName;
        this.gameId = gameId;
    }

    public Record() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
