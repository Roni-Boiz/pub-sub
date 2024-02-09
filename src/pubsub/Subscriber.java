/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pubsub;

/**
 *
 * @author Ronila
 */
public interface Subscriber {
    void receiveMessage(String message);
}
