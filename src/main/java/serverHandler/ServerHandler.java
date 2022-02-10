package serverHandler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.DTOs.ReceiveInvitationDto;
import model.Entities.User;
import services.GameServices;
import services.RecordsServices;
import services.UsersServices;
import utilities.Singleton;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerHandler extends Thread {
    private DataOutputStream dos;
    private DataInputStream dis;
    private Socket clientSocket;
    private String serverHandlerUsername;
    private String message;
    private UsersServices us = new UsersServices();
    private GameServices gs = new GameServices();
    private static int gameId;
    private static String moves;
    private Boolean isFinished = false;
    private RecordsServices rs = new RecordsServices();
    private Singleton singleton = Singleton.getInstance();

    public ServerHandler(Socket s) {
        clientSocket = s;
        singleton.getConnectedClients().add(this);
        try {

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            start();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void close() {
        try {
            dos.close();
            dis.close();
            clientSocket.close();
            this.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonArray getOnlineObjects() {
        JsonArray jA = new JsonArray();
        List<User> onlineUsers = new ArrayList<>();
        onlineUsers = us.getAllOnlineUsers();
        for (User o : onlineUsers) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userName", o.getUserName());
            jsonObject.addProperty("wins", o.getWins());
            jsonObject.addProperty("losses", o.getLosses());
            jsonObject.addProperty("draws", o.getDraws());
            jsonObject.addProperty("status", o.getStatus());
            jA.add(jsonObject);
        }
        return jA;
    }

    public void sendOnlineUsersToAllClient(JsonArray jA) {
        if (singleton.getConnectedClients().size() > 1) {
            for (ServerHandler sH : singleton.getConnectedClients()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("operation", "refreshUsers");
                obj.add("onlineUsers", jA);
                try {
                    if (sH.clientSocket.isConnected())
                        sH.dos.writeUTF(obj.toString());
                    else {
                        sH.dos.close();

                        sH.dis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendCreatedGameToBothPlayer(String player1, String player2, int gameId) {
        if (singleton.getConnectedClients().size() != 0) {

            List<ServerHandler> playersOfGame = new ArrayList<>();
            playersOfGame.add(getHandlerByUsername(player1));
            playersOfGame.add(getHandlerByUsername(player2));

            singleton.getGamesOn().put(gameId, playersOfGame);
            for (ServerHandler handler : singleton.getGamesOn().get(gameId)) {
                JsonObject createdGameObject = new JsonObject();
                createdGameObject.addProperty("operation", "getCreatedGame");
                createdGameObject.addProperty("gameId", gameId);
                createdGameObject.addProperty("playerX", player1);
                createdGameObject.addProperty("playerO", player2);
                try {
                    if (handler != null)
                        handler.dos.writeUTF(createdGameObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("created game with od = "
                    + gameId + "has been sent to "
                    + player1 + " and "
                    + player2);
        }
    }

    public ServerHandler getHandlerByUsername(String player2) {
        for (ServerHandler sH : singleton.getConnectedClients()) {
            if (player2.equals(sH.serverHandlerUsername))
                return sH;
        }

        return null;
    }

    public void sendInvitation(String player1, ServerHandler player2) {
        if (player2 != null) {
            JsonObject inv = new JsonObject();
            inv.addProperty("operation", "receiveInvitation");
            inv.addProperty("opponentName", player1);

            try {
                player2.dos.writeUTF(inv.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPlayerMove(String player, String move, String sign, boolean gameState, Integer gameId) {
        JsonObject playerMoveObj = new JsonObject();
        playerMoveObj.addProperty("operation", "playerMove");
        playerMoveObj.addProperty("playerName", player);
        playerMoveObj.addProperty("position", move);
        playerMoveObj.addProperty("sign", sign);
        playerMoveObj.addProperty("gameState", gameState);
        playerMoveObj.addProperty("gameId", gameId);
        if (player.equals(singleton.getGamesOn().get(gameId).get(0).serverHandlerUsername)) {
            try {
                singleton.getGamesOn().get(gameId).get(1).dos.writeUTF(playerMoveObj.toString());
                moves += move;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                singleton.getGamesOn().get(gameId).get(0).dos.writeUTF(playerMoveObj.toString());
                moves += move;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void finishGame(String winner, int gameId) {
        if (singleton.getGamesOn().get(gameId) != null) {
            User user1 = us.getUserByName(singleton.getReceiveInvitationDto().getUserName());
            User user2 = us.getUserByName(singleton.getReceiveInvitationDto().getOpponentUserName());
            if (winner.equals("draw")) {
                user1.setDraws(user1.getDraws() + 1);
                user2.setDraws(user2.getDraws() + 1);
                us.updateUser(user1);
                us.updateUser(user2);
                gs.setWinner(gameId, 0);
                us.saveChanges();
            } else {
                if (winner.equals(singleton.getReceiveInvitationDto().getUserName())) {
                    user1.setWins(user1.getWins() + 1);
                    user2.setLosses(user2.getLosses() + 1);
                    us.updateUser(user1);
                    us.updateUser(user2);
                    gs.setWinner(gameId, 1);
                    us.saveChanges();
                } else {
                    user2.setWins(user2.getWins() + 1);
                    user1.setLosses(user1.getLosses() + 1);
                    us.updateUser(user1);
                    us.updateUser(user2);
                    gs.setWinner(gameId, 2);
                    us.saveChanges();
                }
            }
            singleton.setReceiveInvitationDto(new ReceiveInvitationDto());
            singleton.getGamesOn().remove(gameId);
        }
    }

    public void run() {
        while (true) {
            try {
                if (dis != null) {
                    message = dis.readUTF();
                }

                JsonObject object = JsonParser.parseString(message).getAsJsonObject();
                String op = object.get("operation").getAsString();
                System.out.println(op);

                switch (op) {
                    case "login":
                        String password, loginUsername;
                        JsonObject loginObj = new JsonObject();
                        JsonArray onlineObjs = new JsonArray();
                        boolean loginCheck;
                        loginUsername = object.get("user").getAsString();
                        serverHandlerUsername = object.get("user").getAsString();
                        password = object.get("pass").getAsString();
                        loginCheck = us.login(loginUsername, password);
                        us.saveChanges();
                        loginObj.addProperty("operation", "login");
                        loginObj.addProperty("result", loginCheck);
                        try {
                            dos.writeUTF(loginObj.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (loginCheck) {
                            onlineObjs = getOnlineObjects();
                            sendOnlineUsersToAllClient(onlineObjs);
                        }
                        break;
                    case "logout":
                        String username = object.get("user").getAsString();
                        us.logout(us.getUserByName(username));
                        us.saveChanges();
                        JsonArray online = new JsonArray();
                        online = getOnlineObjects();
                        if (online.size() > 1)
                            sendOnlineUsersToAllClient(online);
                        singleton.getConnectedClients().remove(this);
                        close();
                        break;

                    case "signUp":
                        String pw, signUpUsername;
                        JsonObject signUpObj = new JsonObject();
                        signUpObj.addProperty("operation", "signUp");
                        boolean signUpCheck;
                        signUpUsername = object.get("user").getAsString();
                        pw = object.get("pass").getAsString();
                        signUpCheck = us.checkValidation(signUpUsername);
                        if (!signUpCheck) {
                            boolean result = us.createUser(signUpUsername, pw, 0, 0, 0);
                            us.saveChanges();
                            if (result) {
                                signUpObj.addProperty("result", true);
                            }
                        } else {
                            signUpObj.addProperty("result", false);
                        }
                        dos.writeUTF(signUpObj.toString());
                        break;

                    case "invitation":
                        singleton.getReceiveInvitationDto().setUserName(object.get("user").getAsString());
                        singleton.getReceiveInvitationDto().setOpponentUserName(object.get("player2").getAsString());//
                        ServerHandler serverHandler = getHandlerByUsername(singleton.getReceiveInvitationDto().getOpponentUserName());
                        sendInvitation(singleton.getReceiveInvitationDto().getUserName(), serverHandler);
                        break;
                    case "invResponse":

                        String response = object.get("answer").getAsString();
                        JsonObject obj = new JsonObject();
                        JsonObject gameIdObj = new JsonObject();
                        obj.addProperty("operation", "player2Response");
                        if (response.equals("true")) {
                            gameId = gs.startGame(singleton.getReceiveInvitationDto().getUserName(), singleton.getReceiveInvitationDto().getOpponentUserName());
                            gs.saveChanges();
                            obj.addProperty("answer", true);
                            if (gameId != -1) {
                                sendCreatedGameToBothPlayer(singleton.getReceiveInvitationDto().getUserName(),
                                        singleton.getReceiveInvitationDto().getOpponentUserName(), gameId);
                            }
                        } else {
                            gameIdObj.addProperty("gameId", -1);
                            obj.addProperty("answer", false);
                        }

                        dos.writeUTF(obj.toString());
                        dos.writeUTF(gameIdObj.toString());
                        break;
                    case "playerMove":
                        String player, move, sign;
                        Integer gameId;
                        boolean gameState;
                        player = object.get("playerName").getAsString();
                        move = object.get("position").getAsString();
                        sign = object.get("sign").getAsString();
                        gameState = object.get("gameState").getAsBoolean();
                        gameId = object.get("gameId").getAsInt();
                        sendPlayerMove(player, move, sign, gameState, gameId);
                        break;
                    case "gameFinished":
                        String winner;
                        int finishedGameId;
                        isFinished = object.get("isFinished").getAsBoolean();
                        finishedGameId = object.get("gameId").getAsInt();
                        winner = object.get("winner").getAsString();
                        if (isFinished) {
                            finishGame(winner, finishedGameId);
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }
    }
}
