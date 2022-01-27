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
    boolean check ;
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
                JsonObject obj = new JsonObject();
                switch (op) {
                    case "login":
                        username = object.get("user").getAsString();
                        password = object.get("pass").getAsString();
                        check = us.login(username, password);
                        // dos.writeBoolean(check);
                        obj.addProperty("operation", "login");
                        obj.addProperty("result", check);
                        try {
                            System.out.println(obj.toString());
                            dos.writeUTF(obj.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "signUp":
                        username = object.get("user").getAsString();
                        password = object.get("pass").getAsString();
                        wins = 0;
                        losses = 0;
                        draws = 0;
                        isLogged = true;
                        us.createUser(username, password, wins, losses, draws);
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
