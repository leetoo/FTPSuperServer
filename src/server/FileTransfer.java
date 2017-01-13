package server;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * This is a thread class used by the client handler to instantiate a data transfer connection.
 * Created by madsl on 16-Dec-16.
 */
public class FileTransfer extends Thread implements Runnable {

    private ClientHandler client;
    private int socketNumber;
    private String command;

    private ServerSocket socket;
    private Socket clientSocket;
    private OutputStream stream;
    private PrintWriter writer;
    private ObjectOutputStream objStream;
    private boolean terminate = false;

    //TODO: make multiple constructor matching action
    public FileTransfer(ClientHandler client, int socketNumber, String command, ArrayList<File> files) {
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
            writer = new PrintWriter(stream);
            objStream = new ObjectOutputStream(stream);
            System.out.println("Filetransfer socket accepted!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (command.equals("LIST")) {

            String dir =    "dr-xr-xr-x  1 noone    nogroup    148943 Apr 29 2013  10181305 \n" +
                        "dr-xr-xr-x  1 noone    nogroup        53 Aug 30 2012  1 \n" +
                        "-r-xr-xr-x  1 noone    nogroup   2492297 Oct 18 14:05 10181301\n" +
                    "dr-xr-xr-x  1 noone    nogroup     88260 Oct 18 14:10 10181302";
            try {
                stream.write(dir.getBytes());
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.shutDownDataConnection();
        }

        try {
            clientSocket.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    void terminateThread() {
        terminate = true;
    }

    void sendDirectory(ServerUser user) {
        String files = "/A:D user ";
        System.out.println(stream);
        try {
            stream.write(files.getBytes());
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
