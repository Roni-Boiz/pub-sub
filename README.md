# Publisher Subscriber Architecture
Extended version of Client-Server to Publsiher Subscriber using java

<b>Compile</b><br>
`javac -d build/classes src/pubsub/*.java`

<b>Run</b><br>
<b>Server</b> --> `java -cp build/classes pubsub.PubSub` or `java -cp build/classes pubsub.PubSub <port> <backlog> <serverip>` <br>
eg: java -cp build/classes pubsub.PubSub 8080 0 localhost

<b>Client</b> --> `java -cp build/classes pubsub.Client <serverip> <port> <username> <role> <topic1> <topic2> ...` <br>
eg: java -cp build/classes pubsub.Client localhost 8080 John PUBLISHER TOPIC_A TOPIC_B <br>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   java -cp build/classes pubsub.Client localhost 8080 Sam SUBSCRIBER TOPIC_B TOPIC_C
