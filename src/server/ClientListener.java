package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by madsl on 16-Dec-16.
 */

public class ClientListener extends Thread implements Runnable {

    private Socket socket;

    public ClientListener(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader buffReader = new BufferedReader(reader);

                String line = "";
                while ((line = buffReader.readLine()) != null){
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
