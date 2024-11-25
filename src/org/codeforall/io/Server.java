package org.codeforall.io;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private int port;

    private List<ServerWorker> serverWorkers = new ArrayList<>();
    private BufferedWriter outputBufferedWriter;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        Server server = new Server(5555);
        server.startConnection();
    }


    public void startConnection() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started in: " + serverSocket);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection established");
                //setupStream();

                ServerWorker serverWorker = new ServerWorker(clientSocket);
                // add serverWorker to list
                serverWorkers.add(serverWorker);

                // create a new thread
                Thread thread = new Thread(serverWorker);
                thread.start();

            }



        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendToAll(String message) {



        for (int i = 0; i < serverWorkers.size(); i++) {
            ServerWorker s = serverWorkers.get(i);
            s.send(message);
        }


    }

    private class ServerWorker implements Runnable {
        String message = "";
        BufferedReader inputBuffer;

        Socket clientSocket;

        String clientName = "";

        public ServerWorker(Socket socket) {
            clientSocket = socket;

        }


        @Override
        public void run() {

            while (true) {

                try {
                    inputBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    message = inputBuffer.readLine();


                    //System.out.println(message);

                    if (message != null && message.startsWith("/")) {
                        if (message.startsWith("/name")) {
                            clientName = message.split(" ")[1] + ": ";

                        } else if (message.startsWith("/quit")) {
                            sendToAll(clientName.substring(0, clientName.length() - 2) + " left");
                            System.out.println(clientName.substring(0, clientName.length() - 2) + " left");
                            clientSocket.close();
                            break;

                        } else if (message.startsWith("/shout")) {
                            message = message.split(" ")[1];
                            sendToAll(message.toUpperCase());
                        }

                    } else {
                        sendToAll(clientName + message);
                    }


                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }


                //sendToAll(message);
            }
        }



        public void send(String message) {
            this.message = message;
            try {
                PrintWriter buffer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
                buffer.println(message);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
