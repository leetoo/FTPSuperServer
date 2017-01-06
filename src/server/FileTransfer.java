package server;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is a thread class used by the client handler to instantiate a data transfer connection.
 * Created by madsl on 16-Dec-16.
 */
public class FileTransfer extends Thread implements Runnable{

    private ClientHandler client;
    private int socketNumber;
    private String command;

    private ServerSocket socket;
    private Socket clientSocket;
    private OutputStream stream;
    private boolean terminate = false;

    public FileTransfer(ClientHandler client, int socketNumber, String command){
        this.client = client;
        this.socketNumber = socketNumber;
        this.command = command;
    }

    @Override
    public void run() {
        super.run();

        try {
            socket = new ServerSocket(socketNumber);
        } catch (IOException e) {
            System.err.println("Problems instatiating serversocket data conn. " + e.getMessage());
        }
        try {
            clientSocket = socket.accept();
            stream = clientSocket.getOutputStream();
            System.out.println("Filetransfer socket accepted!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(command.equals("LIST")){
            System.out.println("made it");
            String files =  "/user ";
            System.out.println(stream);
            try {
                stream.write(files.getBytes());
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.shutDownDataConnection();
        }
    }


    void terminateThread(){
        terminate = true;
    }

    void sendDirectory(ServerUser user){
        String files =  "/A:D user ";
        System.out.println(stream);
        try {
            stream.write(files.getBytes());
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
