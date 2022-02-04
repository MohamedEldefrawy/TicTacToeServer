package serverHandler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.DTOs.ReceiveInvitationDto;
import model.Entities.User;
import services.GameServices;
import services.RecordsServices;
import services.UsersServices;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serversocket;
    private Socket socket;

    public Server() {

        try {
            serversocket = new ServerSocket(2022);
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
