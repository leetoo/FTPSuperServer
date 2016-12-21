package server;

/**
 * Created by madsl on 16-Dec-16.
 */
public class ServerUser {

    private String username, password;

    public ServerUser(String username, String password){
        this.username = username;
        this.password = password;
    }


    @Override
    public String toString() {
        return username + " helloooo" + password;
    }
}
