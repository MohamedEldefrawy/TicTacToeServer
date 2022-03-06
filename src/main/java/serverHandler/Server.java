package serverHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
            if (listener != null)
                listener.stop();
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}