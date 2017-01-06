package client;

import java.io.*;

import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by madsl on 16-Dec-16.
 */
public class FTPClient {
    //Used to connect to the server
    private static final int SERVER_PORT = 21;
    private static final String SERVER_ADDRESS = "localhost";

    private PrintWriter writer;
    private BufferedReader reader;
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
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            scanner = new Scanner(System.in);
            //Wait for welcome message
            authenticate();

            //Send usernmae
            //Send password

        } catch (IOException e) {
            System.err.println("Problems connecting to server socket: " + e.getMessage());
        }


    }

    private void authenticate() {
        String[] response = getCommand();
        if (response[0].equals("220")){ //welcome message so send password
            System.out.print("Enter username: ");
            String username = scanner.next();
            writer.println("USER " + username);
            writer.flush();
        }else{
            System.out.println("not welcome message");
        }

        response = getCommand();
        if(response[0].equals("331")){
            //Username recieved, need password
            System.out.print("Enter password: ");
            String username = scanner.next();
            writer.println("PASS " + username);
            writer.flush();
        }else{
            System.out.println("not needing password");
        }

        response = getCommand();

        if(response[0].equals("230")){
            System.out.println("YOu are logged in");
        }else{
            System.out.println("not logged in");
        }
    }

    private String[] getCommand(){

        String serverResponse = "";
        String message = "";
        try {
            if((message = reader.readLine()) != null){
                for(int i = 0; i < 3; ++i){
                    //Get the server response code
                    serverResponse += message.charAt(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] array = {serverResponse, message};
        System.out.println(Arrays.toString(array));
        return array;
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
