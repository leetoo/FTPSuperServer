package server;

import com.sun.corba.se.spi.activation.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by madsl on 15-Dec-16.
 */

public class FTPServer {

    /**
     * Starts the server application
     */
    public static void main(String[] args) {
        FTPServer main = new FTPServer();
        main.run();
    }

    private ServerSocket serverSocket;
    private static final int SERVER_PORT = 21;
    //Arraylist for all the users
    private static ArrayList<ServerUser> users;
    //Creating a directory
    private Map<String, ArrayList<String>> directory;
    private ArrayList<String> root;
    private File rootFolder;

    public void run() {

        init();

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server up and running");
        } catch (IOException e) {
            System.err.println("Exception will instantiating the serverSocket: " + e.getMessage());
        }

        while (true) {

            try {
                Socket socket = serverSocket.accept(); //Wait for a client to connect
                System.out.println("accepted");
                //ready output stream to talk with client
                ClientHandler handler = new ClientHandler(socket, this);
                handler.start();

            } catch (IOException e) {
                System.err.println("Exception thrown while connecting client: " + e.getMessage());
            }
        }
    }

    /**
     * Starts up the server by loading user and initialising
     */
    private void init() {
        directory = new HashMap<>();
        users = new ArrayList<>();
        loadUsers();
        loadDirectory();
    }

    private void loadDirectory(){
        //init
        directory = new HashMap<>();
        //Get the root folder
        try {
            rootFolder = new File("./home");
            root = new ArrayList<>();
            directory.put(rootFolder.getName(),root);
        }catch (Exception e){
            System.err.println("Root folder not found! " + e.getMessage());
        }

        File[] listOfFiles = rootFolder.listFiles();

        for(int i = 0; i < listOfFiles.length; ++i){
            if(listOfFiles[i].isFile()){
                root.add(listOfFiles[i].getName());
            }
        }

        System.out.println("Directory: " + directory.toString());
    }



    synchronized Map<String, ArrayList<String>> getDirectory(){
        return directory;
    }

    synchronized boolean authenticateUser(ServerUser user){

        for(ServerUser user1 : users){
            if(user1.getUsername().equals(user.getUsername())
                    && user1.getPassword().equals(user.getPassword())){
                return true;
            }
        }

        return false;
    }
    /**
     * Takes all the users from the "users.txt" file and loads the into the
     * users arrayList
     */
    private void loadUsers(){
        try {
            //init file reader
            BufferedReader reader = new BufferedReader(new FileReader("users"));
            String user;

            while ((user = reader.readLine()) != null){ //Read line for line
                //Divide into username and password
                String[] parts = user.split(";");
                String username = parts[0];
                String password = parts[1];
                //Add users
                ServerUser serverUser = new ServerUser(username,password);
                users.add(serverUser);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found! " + e.getMessage());
        }catch (IOException e){
            System.err.println(e.getMessage());
        }

    }


    /**
     * Saves a user to the "users.txt" file
     * @param username of user
     * @param password of user
     */
    private void saveUser(String username, String password){
        try {
            FileWriter writer = new FileWriter("users"); //Open file
            writer.write(username+";"+password); //Add new user
            //Close up filestream
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
}
