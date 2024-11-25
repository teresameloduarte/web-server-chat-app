package org.codeforall.io;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private String address;
    private int port;

    private Socket socket;
    private BufferedReader inputBufferedReader;
    private BufferedWriter outputBufferedReader;


    public Client(String address, int port){
        this.address = address;
        this.port = port;
    }


    private void setupSocketStreams(){

        try {
            inputBufferedReader = new BufferedReader(new InputStreamReader(System.in));
            outputBufferedReader = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void start(){

        try {
            socket = new Socket(address, port);
            setupSocketStreams();

        } catch (UnknownHostException e) {
            System.out.println("Host unknown: " + e.getMessage());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

            Thread thread = new Thread(new GetMessage(socket));
            thread.start();

            while(true) {
                sendMessage();
            }
    }

    private void sendMessage(){

        String message = "";

        try {
            message = inputBufferedReader.readLine();
            outputBufferedReader.write(message + "\n");
            outputBufferedReader.flush();


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {

        Client client = new Client("localhost", 5555);
        client.start();

    }
}