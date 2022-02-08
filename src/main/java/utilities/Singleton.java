package utilities;

import model.DTOs.ReceiveInvitationDto;
import serverHandler.ServerHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Singleton {

    private static Singleton singleton = null;
    private ArrayList<ServerHandler> connectedClients;
    private Map<Integer, List<ServerHandler>> gamesOn;
    private List<String> gamesSteps;
    private ReceiveInvitationDto receiveInvitationDto;


    private Singleton() {
        connectedClients = new ArrayList<>();
        gamesOn = new HashMap<>();
        gamesSteps = new ArrayList<>();
        receiveInvitationDto = new ReceiveInvitationDto();
    }


    public static Singleton getInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }


    public Map<Integer, List<ServerHandler>> getGamesOn() {
        return gamesOn;
    }

    public void setGamesOn(Map<Integer, List<ServerHandler>> gamesOn) {
        this.gamesOn = gamesOn;
    }

    public List<String> getGamesSteps() {
        return gamesSteps;
    }

    public void setGamesSteps(List<String> gamesSteps) {
        this.gamesSteps = gamesSteps;
    }

    public ReceiveInvitationDto getReceiveInvitationDto() {
        return receiveInvitationDto;
    }

    public void setReceiveInvitationDto(ReceiveInvitationDto receiveInvitationDto) {
        this.receiveInvitationDto = receiveInvitationDto;
    }

    public ArrayList<ServerHandler> getConnectedClients() {
        return connectedClients;
    }

    public void setConnectedClients(ArrayList<ServerHandler> connectedClients) {
        this.connectedClients = connectedClients;
    }
}
