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

    public String validateUser(String username, String password){

        String result = null;

        if(this.username.equals(username)
                && this.password == password){

            result = "230 welcome to our FTP server\n";
            return result;
        }else{ //invalid password or username
            result = "430 - invalid username or password\n";
            return result;
        }
    }
}
