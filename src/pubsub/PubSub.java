/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pubsub;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Ronila
 */
public class PubSub {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Input CLI arguments
        int port = 8080;
        int backlog = 0;
        InetAddress serverIP = null;
        
        if (args.length != 3) {
            System.err.println("Usage: java PubSub <port> <backlog> <server-ip>");
            System.out.println("Initialize using default values:" + " port=" + port + " backlog=" + backlog + " serverIP=" + "localhost");
        }else{
            try {
                port = Integer.parseInt(args[0]);
                backlog = Integer.parseInt(args[1]);
                serverIP = InetAddress.getByName(args[2]);
            } 
            catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default port: " + port);
            }
            catch (UnknownHostException e) {
                System.err.println("Invalid server address: " + e.getMessage());
            }
        }
        
        Server server = new Server(port, backlog, serverIP);
        server.start();
    }
    
}
