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
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server  {
    private static Server server=null;
    private ServerSocket serverSocket;
    private Socket socket;
    private ServerHandler  serverHandler;
    private Thread listener;
    private boolean exit = false;
    boolean socketIsClosed =false;


    private Server() {}

    public static Server getServer(){
        if(server == null){
            server = new Server();
        }
        return server;
    }
    public void startServerHandlerThread() {
        exit = false;
        initServer();
    }

    private void initServer(){
        try {
            serverSocket = new ServerSocket(5005);
            System.out.println(serverSocket.getLocalPort());
            listener = new Thread(() -> {
                while(!exit && !socketIsClosed){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket = serverSocket.accept();
                        serverHandler=new ServerHandler(socket);
                    }catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            listener.start();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void stopServerHandlerThread(){
        System.out.println("closed");
        if(serverHandler!=null)
            serverHandler.close();
        socketIsClosed=true;
        exit=true;
        try {
            listener.stop();
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}