package serverHandler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Entities.User;
import services.GameServices;
import services.UsersServices;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server  {
    ServerSocket serversocket;
    Socket socket;

    public Server() {

        try {
            serversocket = new ServerSocket(5005);
            while (true) {
                socket = serversocket.accept();
                System.out.println("some one connected");
                new ServerHandler(socket);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        new Server();
    }

}
class ServerHandler extends Thread
{

    DataOutputStream dos;
    DataInputStream dis;
    Socket clientSocket;
    String serverHandlerUsername;
   static ArrayList<ServerHandler> connectedClients = new ArrayList<ServerHandler>();
   public ServerHandler(Socket s){
       connectedClients.add(this);
       clientSocket=s;
       try {

           dis = new DataInputStream(s.getInputStream());
           dos = new DataOutputStream(s.getOutputStream());
         //  ps.println("you have connected successfully");
           start();
            }
       catch (IOException e){
           e.printStackTrace();

       }
   }
    public  void close (DataOutputStream  dout, DataInputStream di , Socket ss){
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
    public void sendToAll(JsonArray jA){
        if (connectedClients.size() > 0) {
            for (ServerHandler sH : connectedClients) {
                JsonObject obj = new JsonObject();
                obj.addProperty("operation", "refreshUsers");
                obj.add("onlineUsers", jA);
                try {
                    if (sH.clientSocket.isConnected())
                        sH.dos.writeUTF(obj.toString());
                    else {
                        sH.dos.close();
                        sH.dos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public ServerHandler getHandlerByUsername(String player2){
       for (ServerHandler sH :connectedClients){
           if (player2.equalsIgnoreCase(serverHandlerUsername))
               return sH;
       }

      return null;
    }

    public void sendInvitation(String player1 , ServerHandler player2){
       if(player2 !=null) {
           JsonObject inv = new JsonObject();
           inv.addProperty("operation ", "gameRequest");
           inv.addProperty("playerReqName", player1);

           try {
               player2.dos.writeUTF(inv.toString());
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }



    String player1;
    String message;

    String player2;

    UsersServices us = new UsersServices();
    GameServices gs= new GameServices();


    public void run() {
        while (true) {
            try {
                message = dis.readUTF();
                System.out.println("message from client : " + message);
                if (message == null) {
                    throw new IOException();
                }
                JsonObject object = JsonParser.parseString(message).getAsJsonObject();
                String op = object.get("operation").getAsString();
                System.out.println(op);

                switch (op) {
                    case "login":
                        String password, loginUsername;
                        JsonObject loginObj = new JsonObject();
                        JsonArray onlineObjs = new JsonArray();
                        boolean  loginCheck ;
                        loginUsername = object.get("user").getAsString();
                        serverHandlerUsername=object.get("user").getAsString();
                        password = object.get("pass").getAsString();
                        loginCheck = us.login(loginUsername, password);
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
                            sendToAll(onlineObjs);
                            System.out.println(onlineObjs.toString());
                        }
                        break;
                    case "logout":
                        String username = object.get("user").getAsString();
                        us.logout(us.getUserByName(username));

                        JsonArray online = new JsonArray();
                        online = getOnlineObjects();
                        if (online.size() > 0)
                            sendToAll(online);
                        System.out.println(online.toString());
                        close(dos, dis, clientSocket);
                        break;

                    case "signUp":
                        String passwrd, signUpUsername;
                        JsonObject signUpObj = new JsonObject();
                        signUpObj.addProperty("operation", "signUp");
                        boolean signUpCheck;
                        signUpUsername = object.get("user").getAsString();
                        passwrd = object.get("pass").getAsString();
                        signUpCheck = us.checkValidation(signUpUsername);
                        if (!signUpCheck) {
                            boolean result = us.createUser(signUpUsername, passwrd, 0, 0, 0);
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
                         player1 = object.get("user").getAsString();
                         player2 = object.get("player2").getAsString();

                        break;
                    case "invResponse" :
                        int gameId;
                        String response = object.get("answer").getAsString();
                        JsonObject obj = new JsonObject();
                        obj.addProperty("operation","player2Respnse");

                        if(response.equals("true")){
                          gameId=  gs.startGame(player1,player2);
                          obj.addProperty("answer","true");
                        }
                        else  obj.addProperty("answer","false");
                        dos.writeUTF(obj.toString());
                        break;
                }


            } catch (IOException e) {
                e.printStackTrace();
                close(dos, dis, clientSocket);
            }
        }
    }
}
