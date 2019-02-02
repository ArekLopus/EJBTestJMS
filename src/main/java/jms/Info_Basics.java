package jms;

//	Understanding Messaging
//-MOM (Message-oriented middleware), which has been around for a while, uses a special vocabulary.
//-When a message is sent, the software that stores the message and dispatches it is called a provider (or sometimes a broker).
//-The message sender is called a producer.
//-The location where the message is stored is called a destination.
//-The component receiving the message is called a consumer.
//-Any component interested in a message at that particular destination can consume it.

//-In Java EE, the API that deals with these concepts is called Java Message Service (JMS).
// It has a set of interfaces and classes that allow you to connect to a provider, create a message, send it, and receive it. 
//-JMS doesn’t physically carry messages, it’s just an API; it requires a provider that is in charge of handling messages.
//-When running in an EJB container, Message-Driven Beans (MDBs) can be used to receive messages in a container-managed way. 

//-At a high level, a messaging architecture consists of the following components:
// • A provider: JMS is only an API, so it needs an underlying impl to route messages, that is, the provider (or. a message broker).
//   The provider handles the buffering and delivery of messages.
// • Clients: A client is any Java application or component that produces or consumes a message to/from the provider.
//   “Client” is the generic term for producer, sender, publisher, consumer, receiver, or subscriber.
// • Messages: These are the objects that clients send to or receive from the provider.
// • Administered objects: A message broker must provide administered objects to the client (connection factories and destinations)
//   either through JNDI lookups or injection (as you’ll see later).

//-The messaging provider (broker) enables asynchronous communication by providing a destination where messages can be held until
// they can be delivered to a client. There are two different types of destination, each applying to a specific messaging model:
// • The point-to-point (P2P) model: In this model, the destination used to hold messages is called a queue.
//   When using point-to-point messaging, one client puts a message on a queue, and another client receives the message.
//   Once the message is acknowledged, the message provider removes the message from the queue.
// • The publish-subscribe (pub-sub) model: The destination is called a topic. When using publish subscribe messaging,
//   a client publishes a message to a topic, and all subscribers to that topic receive the message.

//	Point-to-Point
//-In the P2P model, a message travels from a single producer to a single consumer.
// The model is built around the concept of message queues, senders, and receivers.
//-A queue retains the messages sent by the sender until they are consumed, and a sender and a receiver do not have timing dependencies.
//-This means that the sender can produce messages and send them in the queue whenever he likes, and a receiver can consume them whenever he likes. 
//-Once the receiver is created, it will get all the messages that were sent to the queue, even those sent before its creation. 
//-Each message is sent to a specific queue, and the receiver extracts the messages from the queue.
// Queues retain all messages sent until they are consumed or until they expire.
//-Note that P2P doesn’t guarantee messages are delivered in any particular order (the order is not defined).
// A provider might pick them in arrival order, or randomly, or some other way.

//	Publish-Subscribe
//-In the pub-sub model, a single message is sent by a single producer to potentially several consumers.
//-The model is built around the concept of topics, publishers, and subscribers. 
//-Consumers are called subscribers because they first need to subscribe to a topic.
//-The provider manages the subscribing/unsubscribing mechanism as it occurs dynamically.
//-The topic retains messages until they are distributed to all subscribers.
//-Unlike the P2P model, there is a timing dependency between publishers and subscribers; subscribers do not receive messages sent
// prior to their subscription, and, if the subscriber is inactive for a specified period, it will not receive past messages
// when it becomes active again. Note that this can be avoided, because the JMS API supports the concept of a durable subscriber.
//-Multiple subscribers can consume the same message. The pub-sub model can be used for broadcast-type applications, in which
// a single message is delivered to several consumers.

//	Administered Objects
//-Administered objects are objects that are configured administratively, as opposed to programmatically.
//-The message provider allows these objects to be configured, and makes them available in the JNDI namespace. 
//-Like JDBC datasources, administered objects are created only once. The two types of administered objects: 
// • Connection factories: Used by clients to create a connection to a destination.
// • Destinations: Message distribution points that receive, hold, and distribute messages.
//   Destinations can be queues (P2P) or topics (pub-sub).
//-Clients access these objects through portable interfaces by looking them up in the JNDI namespace or through injection.
//-In GlassFish, there are several ways to create these objects as you’ll later see:
// by using the administration console, the asadmin CLI, or the REST interface.
//-Since JMS 2.0 you can even use the @JMSConnectionFactoryDefinition and @JMSDestinationDefinition
// annotations to define programmatically these objects.




//jms/__defaultConnectionFactory
//jms/__defaultQueue

//acknowledgeMode 			The acknowledgment mode (default is AUTO_ACKNOWLEDGE)
//messageSelector 			The message selector string used by the MDB
//destinationType 			The destination type, which can be TOPIC or QUEUE
//destinationLookup 		The lookup name of an administratively-defined Queue or Topic
//connectionFactoryLookup 	The lookup name of an administratively defined ConnectionFactory
//destination 				The name of the destination.
//subscriptionDurability 	The subscription durability (default is NON_DURABLE)
//subscriptionName 			The subscription name of the consumer
//shareSubscriptions 		Used if the message-driven bean is deployed into a clustered
//clientId 					Client identifier that will be used when connecting to the JMS provider


public class Info_Basics {}
