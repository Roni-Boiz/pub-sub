/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pubsub;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ronila
 */
public class ClientHandler implements Runnable, Subscriber {
    private final Socket clientSocket;
    private final Publisher publisher;
    private final Server server;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final String clientRole;
    private ArrayList<String> clientTopics = new ArrayList<>();

    public ClientHandler(Socket clientSocket, Publisher publisher, Server server) throws IOException {
        this.clientSocket = clientSocket;
        this.publisher = publisher;
        this.server = server;
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.clientRole = reader.readLine();
        String[] topics = reader.readLine().split(" ");	
        for (int i = 0; i < topics.length; i++) {
            clientTopics.add(topics[i]);
        }
        if(clientRole.equalsIgnoreCase("SUBSCRIBER")){
            for (String topic : clientTopics) {
                this.publisher.addSubscriber(topic, this);
            } 
        }
    }

    @Override
    public void run() {
        try {
            String inputMessage;
            while (!clientSocket.isClosed()) {
                
                inputMessage = reader.readLine();
                if (inputMessage == null || inputMessage.equalsIgnoreCase("TERMINATE") || inputMessage.equalsIgnoreCase("SERVER SHUTTING DOWN")) {
                    System.out.println("Client disconnected: " + clientSocket);
                    if(clientRole.equalsIgnoreCase("SUBSCRIBER")){
                        for (String topic : clientTopics) {
                            this.publisher.removeSubscriber(topic, this);
                        } 
                    }
                    server.removeClient(this);
                    clientSocket.close();
                    break;
                }
                
                System.out.println(inputMessage);

                // Echo the message back to all subscribers
                for (String topic : clientTopics) {
                    publisher.publishMessage(topic, inputMessage.substring(inputMessage.indexOf(':') + 2).trim());
                }      
            }
        } 
        catch (IOException e) {
            // Handle SocketException when the client disconnects abruptly
            System.out.println("Client disconnected abruptly: " + clientSocket);
            if(clientRole.equalsIgnoreCase("SUBSCRIBER")){
                for (String topic : clientTopics) {
                    this.publisher.removeSubscriber(topic, this);
                }
            }
            server.removeClient(this);
        }
        finally {
            try {
                if(!clientSocket.isClosed()){
                    clientSocket.close();
                }   
            } catch (IOException e) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    // Add this method to signal that the client thread has completed
    public void join(int timeoutMillis) throws InterruptedException {
        Thread.currentThread().join(timeoutMillis);
    }
    
    // Method to send a message to this specific client
    public void writeMessage(String message) {
        try {
            if(!clientSocket.isClosed()){
                writer.write(message);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void receiveMessage(String message) {
        writeMessage(message);
    }
}
