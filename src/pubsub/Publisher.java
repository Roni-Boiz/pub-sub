/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pubsub;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Ronila
 */
public class Publisher {
    private List<Subscriber> subscribers = new ArrayList<>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void publishMessage(String message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.receiveMessage(message);
        }
    }
}
