package server;

import java.io.*;
import java.net.Socket;

/**
 * Created by madsl on 16-Dec-16.
 */

public class ClientHandler extends Thread implements Runnable {

    private Socket socket;
    private FTPServer server;

    private static final int COMMAND = 0;
    private static final int MESSAGE = 1;

    private BufferedReader reader;
    private PrintWriter writer;

    private String[] array;
    //For user validation
    private String username, password;
    private ServerUser user;
    private boolean isUserValidate = false;


    public ClientHandler(Socket socket, FTPServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        super.run();
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //welcome message to client
        writer.println("220 welcome to my server");
        writer.flush();
        //get clients credentials and authenticate
        validateUser();
        //Authentication of user done! Proceed

    }

    /**
     * Calls handlePassword and handleUsername. Method then verifies the user with the
     * server.
     */
    private void validateUser(){

        while(!isUserValidate) {
            handleUsername();
            handlePassword();
            user = new ServerUser(username, password);
            if (server.authenticateUser(user)) { //User is authenticated
                isUserValidate = true;
            } else {
                //Invalid credentials
                //TODO: Credentials invalid, how to handle?
                validateUser();
            }
        }

    }

    //TODO: what happens when wrong password? send user and then password again? or just pass?
    //TODO: Same with username
    /**
     * Gets password from client and handles it
     */
    private void handlePassword() {
        array = getCommand();
        //Handle client message
        if (array[0].equals("PASS")){
            password = array[1];
        }else{
            writer.println("430 you need to log in");
            handlePassword();
        }

    }

    /**
     * Gets username from client and handles it
     */
    private void handleUsername() {
        String username = "";
        //get username
        array = getCommand();
        //handle client message
        if (array[0].equals("USER")){
            username = array[1]; //Set username
            writer.println("331 need password");
        }else{
            writer.println("530 you need to log in");
            handleUsername();
        }
        writer.flush();
    }

    /**
     * Method that takes client input and parses it to two strings: command and message
     * @returns an array where index 0 is the command and index 1 is the message
     */
    private String[] getCommand(){
        String message = "";
        String command = "";
        //TODO: all commands 4 letters?
        try {
            //retrive message from client
            message = reader.readLine();
            //get the first four characters
            for (int i = 0; i < 4; ++i) {
                command += message.charAt(i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] array = {command,message};
        return array;
    }
}