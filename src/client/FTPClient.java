package client;

import java.io.*;

import java.net.Socket;
import java.util.Scanner;

/**
 * Created by madsl on 16-Dec-16.
 */
public class FTPClient {
    //Used to connect to the server
    private static final int SERVER_PORT = 21;
    private static final String SERVER_ADDRESS = "localhost";

    private PrintWriter writer;
    private Scanner scanner;

    private boolean terminate = false;

    public static void main(String[] args) {
        new FTPClient().run();
    }

    private void run() {

        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to FTP server");
            //Making communication possible
            writer = new PrintWriter(socket.getOutputStream());
            scanner = new Scanner(System.in); //handling user input
        } catch (IOException e) {
            System.err.println("Problems connecting to server socket: " + e.getMessage());
        }


    }

    /**
     * CWD - change working directory
     * CDUP - change to parent directory
     * PWD - current working directory
     * DELE - del
     * RMD - delete this directory
     * MKD - make directory
     */
}
