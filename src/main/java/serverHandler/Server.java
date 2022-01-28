package serverHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import services.UsersServices;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server  {
    ServerSocket serversocket;
    Socket socket;

    public Server() {

        try {
            serversocket = new ServerSocket(5005);
            socket = serversocket.accept();
            System.out.println("some one connected");
            new ServerHandler(socket);
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
    Socket ClientSocket;

   static ArrayList<ServerHandler> connectedClients = new ArrayList<ServerHandler>();
   public ServerHandler(Socket s){
       connectedClients.add(this);
       ClientSocket=s;
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

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    String username;
    String password;
    String message;
    int  wins;
    int losses;
    int draws;
    String player2;
    Boolean isLogged ;
    UsersServices us = new UsersServices();


    public void run() {
        while (true) {
            try {
                message = dis.readUTF();
                if (message == null) {
                    throw new IOException();
                }
                JsonObject object = JsonParser.parseString(message).getAsJsonObject();
                String op = object.get("operation").getAsString();
                System.out.println(op);

                switch (op) {
                    case "login":
                        JsonObject loginObj = new JsonObject();
                        boolean  loginCheck ;
                        username = object.get("user").getAsString();
                        password = object.get("pass").getAsString();
                        loginCheck = us.login(username, password);
                        // dos.writeBoolean(check);
                        loginObj.addProperty("operation", "login");
                        loginObj.addProperty("result", loginCheck);
                        try {
                            System.out.println(loginObj.toString());
                            dos.writeUTF(loginObj.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "signUp":
                        JsonObject signUpObj = new JsonObject();
                        signUpObj.addProperty("operation","signUp");
                        boolean  signUpCheck ;
                        boolean  insertCheck ;
                        username = object.get("user").getAsString();
                        password = object.get("pass").getAsString();
                        wins = 0;
                        losses = 0;
                        draws = 0;
                       // isLogged = true;
                       signUpCheck= us.checkValidation(username);
                       if (signUpCheck) {
                           us.createUser(username, password, wins, losses, draws);
                          insertCheck = us.checkValidation(username);
                          if(!insertCheck)
                          {

                              signUpObj.addProperty("result",true);
                          }
                       }
                       else{
                           signUpObj.addProperty("result",false);
                       }
                        dos.writeUTF(signUpObj.toString());
                       break;
                    case "invitation":
                        //  username = object.get("user").getAsString();
                        // player2 = object.get("player2").getAsString();

                }


            } catch (IOException e) {
                e.printStackTrace();
                close(dos, dis, ClientSocket);
            }
        }
    }
}
