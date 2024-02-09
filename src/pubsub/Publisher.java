/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Ronila
 */
public class Publisher {
    private final Map<String, List<Subscriber>> subscribersByTopic = new HashMap<>();

    public void addSubscriber(String topic, Subscriber subscriber) {
        subscribersByTopic.computeIfAbsent(topic, k -> new ArrayList<>()).add(subscriber);
    }

    public void removeSubscriber(String topic, Subscriber subscriber) {
        List<Subscriber> subscribers = subscribersByTopic.get(topic);
        if (subscribers != null) {
            subscribers.remove(subscriber);
        }
    }

    public void publishMessage(String topic, String message) {
        List<Subscriber> subscribers = subscribersByTopic.get(topic);
        if (subscribers != null) {
            for (Subscriber subscriber : subscribers) {
                subscriber.receiveMessage(message);
            }
        }
    }
}
