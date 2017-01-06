package server;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by madsl on 16-Dec-16.
 */
public class ServerUser {

    private String username, password;
    private StringBuilder currentWorkingDirectory;

    ServerUser(String username, String password){
        this.username = username;
        this.password = password;
        this.currentWorkingDirectory = new StringBuilder();
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    StringBuilder getCurrentWorkingDirectory() {
        return currentWorkingDirectory;
    }

    void setCurrentWorkingDirectory(String currentWorkingDirectory) {
        this.currentWorkingDirectory.append(currentWorkingDirectory);
    }

    //Go back
    //Go further


    @Override
    public String toString() {
        return password;
    }
}
