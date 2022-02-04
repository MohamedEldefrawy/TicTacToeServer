package serverHandler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.DTOs.ReceiveInvitationDto;
import model.Entities.User;
import services.GameServices;
import services.UsersServices;

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
    private static ArrayList<ServerHandler> connectedClients = new ArrayList<ServerHandler>();
    private List<ServerHandler> inGameHandlers = new ArrayList<>();
    private String message;
    private static ReceiveInvitationDto receiveInvitationDto = new ReceiveInvitationDto();
    private UsersServices us = new UsersServices();
    private GameServices gs = new GameServices();

    public ServerHandler(Socket s) {
        clientSocket = s;
        connectedClients.add(this);

        try {

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            //  ps.println("you have connected successfully");
            start();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void close(DataOutputStream dout, DataInputStream di, Socket ss) {
        try {
            dout.close();
            di.close();
            ss.close();
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
        if (connectedClients.size() > 1) {
            for (ServerHandler sH : connectedClients) {
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
        if (connectedClients.size() != 0) {

            inGameHandlers.add(getHandlerByUsername(player1));
            inGameHandlers.add(getHandlerByUsername(player2));
            connectedClients.forEach(serverHandler -> System.out.println("server Handlers online = " + serverHandler.serverHandlerUsername));
            inGameHandlers.forEach(serverHandler -> System.out.println(serverHandler.serverHandlerUsername));
            for (ServerHandler handler : inGameHandlers) {
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
        }
    }

    public ServerHandler getHandlerByUsername(String player2) {
        for (ServerHandler sH : connectedClients) {
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

    public void sendPlayerMove(String player, String move) {
        JsonObject playerMoveObj = new JsonObject();
        playerMoveObj.addProperty("PlayerName", player);
        playerMoveObj.addProperty("move", move);
        if (player.equalsIgnoreCase(inGameHandlers.get(0).serverHandlerUsername)) {
            try {
                inGameHandlers.get(1).dos.writeUTF(playerMoveObj.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                inGameHandlers.get(0).dos.writeUTF(playerMoveObj.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void run() {
        while (true) {
            try {
                if (dis != null) {
                    message = dis.readUTF();
                    System.out.println("message from client : " + message);
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
                            System.out.println(loginObj.toString());
                            dos.writeUTF(loginObj.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (loginCheck) {
                            onlineObjs = getOnlineObjects();
                            sendOnlineUsersToAllClient(onlineObjs);
                            System.out.println(onlineObjs.toString());
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
                        System.out.println(online.toString());
                        connectedClients.remove(this);
                        close(dos, dis, clientSocket);
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
                        System.out.println("response has been sent " + signUpObj.toString());
                        break;

                    case "invitation":
                        receiveInvitationDto.setUserName(object.get("user").getAsString());
                        receiveInvitationDto.setOpponentUserName(object.get("player2").getAsString());//
                        ServerHandler serverHandler = getHandlerByUsername(receiveInvitationDto.getOpponentUserName());
                        sendInvitation(receiveInvitationDto.getUserName(), serverHandler);
                        System.out.println("from invitation user name = " + receiveInvitationDto.getUserName());
                        System.out.println("from invitation opponent name = " + receiveInvitationDto.getOpponentUserName());
                        break;
                    case "invResponse":
                        int gameId;
                        String response = object.get("answer").getAsString();
                        JsonObject obj = new JsonObject();
                        JsonObject gameIdObj = new JsonObject();
                        System.out.println("from invResponse user name = " + receiveInvitationDto.getUserName());
                        System.out.println("from invResponse opponent name = " + receiveInvitationDto.getOpponentUserName());
                        obj.addProperty("operation", "player2Response");
                        if (response.equals("true")) {
                            gameId = gs.startGame(receiveInvitationDto.getUserName(), receiveInvitationDto.getOpponentUserName());
                            gs.saveChanges();
                            obj.addProperty("answer", true);
                            sendCreatedGameToBothPlayer(receiveInvitationDto.getUserName(), receiveInvitationDto.getOpponentUserName(), gameId);
                        } else {
                            gameIdObj.addProperty("gameId", -1);
                            obj.addProperty("answer", false);
                        }

                        dos.writeUTF(obj.toString());
                        dos.writeUTF(gameIdObj.toString());
                        break;
                    case "playerMove":
                        String player, move;
                        player = object.get("playerName").getAsString();
                        move = object.get("position").getAsString();
                        sendPlayerMove(player, move);
                }


            } catch (IOException e) {
                e.printStackTrace();
                close(dos, dis, clientSocket);
            }
        }
    }
}
