/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pubsub;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ronila
 */
public class Server {
    private final int port;
    private final int backlog;
    private final InetAddress serverIP;
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
   
    public Server(int port, int backlog, InetAddress serverIP) {
        this.port = port;
        this.backlog = backlog;
        this.serverIP = serverIP;
    }

    public void start() {
        
        // Register a shutdown hook to handle force stop or terminal close
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            
            System.out.println("Server is shutting down...");
            
            // Notify clients about the server shutdown
            broadcast("SERVER SHUTTING DOWN");

            // Wait for client handlers to finish
            for (ClientHandler client : clients) {
                try {
                    client.join(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Close the server socket and disconnect clients
            stop();
        }));
        
        try {
            serverSocket = new ServerSocket(port, backlog, serverIP);
            System.out.println("Server is listening on port " + port);

            Publisher publisher = new Publisher();
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.toString());
                
                ClientHandler clientHandler = new ClientHandler(clientSocket, publisher, this);
                clients.add(clientHandler);
                // Create a new thread to handle the client
                new Thread(clientHandler).start();
            } 
               
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
    
    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
    
    private void stop() {
        try {
            if (!serverSocket.isClosed()) {
                System.out.println("Terminated");
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.writeMessage(message);
        }
    }
    
}
