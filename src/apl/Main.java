package apl;

import server.FTPServer;

/**
 * Created by madsl on 09-Dec-16.
 */
public class Main {

    public static void main(String[] args) {

        FTPServer ftp = new FTPServer();
        ftp.run(); //Start the server

    }


    /*
    * SERVER RETURN CODES
    * 200 - all good
    * 230 - user is logged in, procceed
    * 231 - user logged out
    * 331 - username all good, need password
    * 430 - invalid username or password
    *
    *CLIENT
    * USER - username
    * PASS - password
    * PASV - passive mode
    *
    * */
}
