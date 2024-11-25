package org.codeforall.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class GetMessage implements Runnable {

    Socket socket;
    private BufferedReader reader;

    public GetMessage(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        // read from server - inputBufferedReader.readline()
        // sout this message

        String message = "";

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {

                message = reader.readLine();
                System.out.println(message);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
