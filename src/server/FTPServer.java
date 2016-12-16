package server;

import server.ClientListener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by madsl on 15-Dec-16.
 */

public class FTPServer {

    private ServerSocket serverSocket;
    private static final int SERVER_PORT = 21;

    public void run(){

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Socket = " + serverSocket.getLocalPort() + " address: " + serverSocket.getLocalSocketAddress());
        } catch (IOException e) {
            System.err.println("Exception will instantiating the serverSocket: " + e.getMessage());
        }

        while (true){

            try {

                Socket socket = serverSocket.accept(); //Wait for a client to connect
                System.out.println("accepteeeed");
                //ready output stream to talk with client
                OutputStream os = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(os);
                ///write welcome message to the client
                writer.write("200\n");
                writer.flush();
                //Open for client listener
                ClientListener listener = new ClientListener(socket);
                listener.run();

            } catch (IOException e) {
                System.err.println("Exception thrown while connecting client: " + e.getMessage());
            }

        }
    }
}
