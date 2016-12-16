import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by madsl on 15-Dec-16.
 */

public class FTPServer {

    private ServerSocket serverSocket;
    private static final int SERVER_PORT = 21;

    void run(){

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Socket = " + serverSocket.getLocalPort() + " address: " + serverSocket.getLocalSocketAddress());
        } catch (IOException e) {
            System.err.println("Exception will instantiating the serverSocket: " + e.getMessage());
        }

        while (true){

            try {
                Socket client = serverSocket.accept(); //Wait for a client to connect
            } catch (IOException e) {
                System.err.println("Exception thrown while connecting client: " + e.getMessage());
            }

            //TODO: client is connected so start the command input thread
        }
    }
}
