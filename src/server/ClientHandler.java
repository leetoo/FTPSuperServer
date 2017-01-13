package server;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

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
    //Boolean for account state
    private boolean isLoggedIn = true;
    //Data connection thread
    private FileTransfer dataConnection = null;
    private int passivePortNumber;

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
        //Authentication of user done!
        handleClientResponse();
    }

    private void handleClientResponse() {

        while (isLoggedIn) {
            String[] response = getCommand();
            //Is valid command?
            switch (response[0]) {
                case "SYST": //client wants to know about system (server)
                    //A: stand for ASCII text (normal characters)
                    //T: is for telnet format control. Communication between server and client
                    //Shall follow this protocol
                    writer.println("Windows Type: AT\n");
                    break;
                case "FEAT": //clients wants to know what features the server has
                    //TODO: passive mode a feature?
                    writer.println("211 no features\n");
                    break;
                case "PWD":
                    writer.println("257 " + user.getCurrentWorkingDirectory()+"/user/homeboy" + "\n");
                    break;
                case "CWD":
                    user.setCurrentWorkingDirectory(response[1]);
                    writer.println("200 curretn directory is: " + response[1]);
                case "TYPE": //User wants to set transfer mode
                    writer.println("200 TYPE is now binary\n"); //TODO: how to implement binary?
                    break;
                case "PASV": //Passive mode
                    int[] ports = getPorts();
                    passivePortNumber = (ports[0] * 256) + ports[1];
                    writer.println("227 Entering Passive Mode (127,0,0,1," + ports[0] + "," + ports[1] + ")\n");
                    break;
                case "PORT": //User wants to connect to a specific port for data transfer
                    if (dataConnection != null) { //Already a dataConnection established
                        dataConnection.terminateThread(); //Stop current file transfer thread
                        dataConnection = null;
                    }
                    //get the port numbers
                    String[] portInformation = response[1].split(",");
                    int p1 = Integer.parseInt(portInformation[4]);
                    int p2 = Integer.parseInt(portInformation[5]);
                    int portNumber = ((p1 * 256) + p2);
                    //Inform client
                    writer.println("200 port ready\n");
                    break;
                case "LIST":
                    dataConnection = new FileTransfer(this,passivePortNumber,"LIST");
                    writer.println("150 opening connection");
                    dataConnection.start();
                    break;
                default:
                    writer.println("502 commands not implemented");
                    break;
            }
            writer.flush();
        }
    }

    public void shutDownDataConnection(){
        dataConnection = null;
        writer.println("226 Closing data connection\n");
        writer.flush();
    }

    /**
     * Method that takes client input and parses it to two strings: command and message
     *
     * @returns an array where index 0 is the command and index 1 is the message
     */
    private String[] getCommand() {
        String message;

        try {
            //retrieve message from client
            message = reader.readLine();
            array = message.split(" ");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    /**
     * Calls handlePassword and handleUsername. Method then verifies the user with the
     * server.
     */
    private void validateUser() {

        while (!isUserValidate) {
            handleUsername();
            handlePassword();
            user = new ServerUser(username, password);
            if (server.authenticateUser(user)) { //User is authenticated
                isUserValidate = true;
                writer.println("230 you are logged in");
                writer.flush();
            } else {
                //Invalid credentials
                System.out.println("Invalid user");
                //TODO: what happens with wrong password username?
            }
        }

        user.setCurrentWorkingDirectory("/home");
    }

    //TODO: what happens when wrong password? send user and then password again? or just pass?
    //TODO: Same with username

    /**
     * Gets password from client and handles it
     */
    private void handlePassword() {
        array = getCommand();
        //Handle client message
        if (array[0].equals("PASS")) {
            password = array[1];
        } else {
            writer.println("430 you need to log in");
            getCommand();
        }

    }

    /**
     * Gets username from client and handles it
     */
    private void handleUsername() {
        //get username
        array = getCommand();
        //handle client message
        if (array[0].equals("USER")) {
            username = array[1]; //Set username
            writer.println("331 need password");
        } else {
            writer.println("530 you need to log in");
            getCommand();
        }
        writer.flush();
    }

    public int[] getPorts() {
        Random random = new Random();
        int p1 = (random.nextInt(100 - 1 + 1) + 1);
        int p2 = (random.nextInt(100 - 1 + 1) + 1);
        int[] ports = {p1, p2};
        return ports;
    }
}