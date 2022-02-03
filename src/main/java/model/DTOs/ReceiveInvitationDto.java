package model.DTOs;

public class ReceiveInvitationDto {
    private String userName;
    private String opponentUserName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOpponentUserName() {
        return opponentUserName;
    }

    public void setOpponentUserName(String opponentUserName) {
        this.opponentUserName = opponentUserName;
    }
}
