package jms.reliability_mechanisms;

//	Reliability Mechanisms
//-What if you rely heavily on JMS and need to ensure reliability or other advanced features? 
//-JMS defines several levels of reliability to ensure your message is delivered, even if the provider crashes
// or is under load, or if destinations are filled with messages that should have expired. 
//-The mechanisms for achieving reliable message delivery are as follows:
// • Filtering messages: Using selectors you can filter messages you want to receive
// • Setting message time-to-live: Set an expiration time on messages so they are not delivered if they are obsolete
// • Specifying message persistence: Specify that messages are persistent in the event of a provider failure
// • Controlling acknowledgment: Specify various levels of message acknowledgment
// • Creating durable subscribers: Ensure messages are delivered to an unavailable subscriber in a pub-sub model
// • Setting priorities: Set the priority for delivering a message


//	Filtering messages
//-Some messaging applications need to filter the messages they receive.
//-When a message is broadcast to many clients, it becomes useful to set criteria so that it is only consumed by certain receivers. 
// This eliminates both the time and bandwidth the provider would waste transporting messages to clients that don’t need them.
//-Messages are composed of 3 parts: header, properties, and body.
// The header contains a fixed number of fields (the message metadata), and the properties are a set of custom name-value pairs
// that the application can use to set any values.
//-Selection can be done on those two areas:
// Producers set one or several property values or header fields, and
// the consumer specifies message selection criteria using selector expressions. 
//-Only messages that match the selector are delivered.
//-Message selectors assign the work of filtering messages to the JMS provider, rather than to the application.
//-A message selector is a string that contains an expression.
//-The syntax of the expression is based on a subset of the SQL92 conditional expression syntax and looks like this:
//	context.createConsumer(queue, "JMSPriority < 6").receive();
//	context.createConsumer(queue, "JMSPriority < 6 AND orderAmount < 200").receive();
//	context.createConsumer(queue, "orderAmount BETWEEN 1000 AND 2000").receive();
//-In the preceding code, a consumer is created with the JMSContext.createConsumer(), passing a selector string.
// This string can use header fields (JMSPriority < 6) or custom properties (orderAmount < 200).
//-The producer sets these properties into the message:
//	context.createTextMessage().setIntProperty("orderAmount", 1530);
//	context.createTextMessage().setJMSPriority(5);
//-Selector expression can use logical operators (NOT, AND, OR), comparison operators (=, >, >=, <, <=, <>),
// arithmetic operators (+, -, *, /), expressions ([NOT] BETWEEN, [NOT] IN, [NOT] LIKE, IS [NOT] NULL), and so on.


//	Message Time-to-Live
//-Under heavy load, a time-to-live can be set on messages to ensure that the provider will remove them from the destination
// when they become obsolete, by either using the JMSProducer API or setting the JMSExpiration header field.
//-The JMSProducer has a setTimeToLive() that takes a number of milliseconds:
//	context.createProducer().setTimeToLive(1000).send(queue, message);


//https://stackoverflow.com/questions/43459812/difference-between-persistent-and-non-persistent-delivery
//	Specifying Message Persistence
//-JMS supports two modes of message delivery:		persistent and nonpersistent. 
//-Persistent delivery (defaulr) ensures that a message is delivered only once to a consumer, 
//-Nonpersistent delivery requires a message be delivered once at most.
//-Persistent delivery is more reliable, but at a performance cost, as it prevents losing a message if a provider failure occurs.
//-The delivery mode can be specified by using the setDeliveryMode() of the JMSProducer interface:
//	context.createProducer().setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(queue, message);


//	Controlling Acknowledgment
//-Sometimes, you will want a receiver to acknowledge the message has been received.
//-An acknowledgment phase can be initiated either by the JMS provider or by the client, depending on the acknowledgment mode.

//-In transactional sessions, acknowledgment happens automatically when a transaction is committed.
// If a transaction is rolled back, all consumed messages are redelivered.
//-But in nontransactional sessions, an acknowledgment mode must be specified:
// • AUTO_ACKNOWLEDGE: The session automatically acknowledges the receipt of a message 
// • CLIENT_ACKNOWLEDGE: A client acknowledges a message by explicitly calling the Message.acknowledge() method
// • DUPS_OK_ACKNOWLEDGE: This option instructs the session to lazily acknowledge the delivery of messages. This is likely to result in the delivery of some duplicate messages if the JMS provider fails, so it should be used only by consumers that can tolerate duplicate messages. If the message is redelivered, the provider sets the value of the JMSRedelivered header field to true

//-The following code uses the @JMSSessionMode to set the acknowledgment mode to the JMSContext on the producer.
// The consumer explicitly acknowledges the message by calling the acknowledge() method:
//	@JMSConnectionFactory("jms/connectionFactory")		// Producer
//	@JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
//	private JMSContext context;
//	context.createProducer().send(queue, message);		
//	message.acknowledge();								// Consumer


//	Creating Durable Consumers
//-The disadvantage of using the pub-sub model is that a message consumer must be running when the messages are sent to the topic;
// otherwise, it will not receive them. By using durable consumers, the JMS API provides a way to keep messages in the topic
// until all subscribed consumers receive them. With durable subscription, the consumer can be offline for some time, but,
// when it reconnects, it receives the messages that arrived during its disconnection.
//-To achieve this, the client creates a durable consumer using the JMSContext:
//	context.createDurableConsumer(topic, "javaee7DurableSubscription").receive();

//-At this point, the client program starts the connection and receives messages. The name javaee7DurableSubscription is used
// as an identifier of the durable subscription. Each durable consumer must have a unique ID,
// resulting in the declaration of a unique connection factory for each potential, durable consumer.


//	Setting Priorities
//-You can use message priority levels to instruct the JMS provider to deliver urgent messages first. JMS defines ten priority
// values, with 0 as the lowest and 9 as the highest. You can specify the priority value by using the
// setPriority() of the JMSProducer:
//	context.createProducer().setPriority(2).send(queue, message);
//-Most of these methods return the JMSProducer to allow method calls to be chained together, allowing a fluid prog style. Fe:
//	context.createProducer()
//	    .setPriority(2)
//	    .setTimeToLive(1000)
//	    .setDeliveryMode(DeliveryMode.NON_PERSISTENT)
//	    .send(queue, message);

//-setAsync(CompletionListener completionListener), setDeliveryDelay(long deliveryDelay), setDeliveryMode(int deliveryMode)
//-setDisableMessageID(boolean value), setDisableMessageTimestamp(boolean value), setJMSCorrelationID(String correlationID)
//-setJMSCorrelationIDAsBytes(byte[] correlationID), setJMSReplyTo(Destination replyTo), setJMSType(String type)
//-setPriority(int priority), setProperty(String name, X value), setTimeToLive(long timeToLive)

public class AnInfo {}