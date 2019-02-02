package jms;

//	Simplified API
//-JMS 2.0 introduces a simplified API, which consists mainly of 3 new interfaces (JMSContext, JMSProducer and JMSConsumer).
//-These interfaces rely internally on the ConnectionFactory and other classic APIs but leave the boilerplate code aside.
// • JMSContext: active connection to a JMS provider and a single-threaded context for sending and receiving messages
// • JMSProducer: object created by a JMSContext that is used for sending messages to a queue or topic
// • JMSConsumer: object created by a JMSContext that is used for receiving messages sent to a queue or topic
//-Thanks to the new JMSRuntimeException, which is an unchecked exception,
// the code to send or receive a message is much easier to write and read.

//	JMSContext
//-The JMSContext is the main interface in the simplified JMS API introduced by JMS 2.0. 
//-It combines the functionality of two separate objects from the JMS 1.1 classic API: a Connection (the physical link
// to the JMS provider) and a Session (a single-threaded context for sending and receiving messages).
//-A JMSContext may be created by the application by calling one of several createContext methods on a ConnectionFactory. 
//-If the application is running in a container (EJB or Web), the JMSContext can be injected using the @Inject.
//-When an application needs to send messages it uses the createProducer() to create a JMSProducer,
// which provides methods to configure and send messages. Messages may be sent either synchronously or asynchronously.
//-To receive messages, an application can use one of several createConsumer methods to create a JMSConsumer. 
//	void start(), void stop(), void close(), void commit(), void rollback(), BytesMessage createBytesMessage()
//	MapMessage createMapMessage(), Message createMessage(), ObjectMessage createObjectMessage(),
//	StreamMessage createStreamMessage(), TextMessage createTextMessage(), Topic createTopic(String topicName)
//	Queue createQueue(String queueName), JMSProducer createProducer(), JMSConsumer createConsumer(Destination destination),
//	JMSConsumer createConsumer(Destination dest, String messageSelector), JMSContext createContext(int sessMode)

//	JMSProducer
//-A JMSProducer is used to send messages on behalf of a JMSContext.
//-It provides various send methods to send a message to a specified destination. 
//-An instance of JMSProducer is created by calling the createProducer() on a JMSContext. 
//-It also provides methods to allow send options, message properties and message headers (see Figure 13-8) to be specified prior to sending the message. 
//	get/set[Type]Property, JMSProducer clearProperties(), Set<String> getPropertyNames(), boolean propertyExists(String name),
//	get/set[Message Header], JMSProducer send(Destination destination, Message message), JMSProducer send(Destination d, String body)

//	JMSConsumer
//-A JMSConsumer is used to receive messages from a queue or topic. 
//-It is created with one of the createConsumer methods on a JMSContext by passing a Queue or a Topic. 
//-As you will later see, a JMSConsumer can be created with a message selector so it can restrict messages delivered.
//-A client may receive a message synchronously or asynchronously as they arrive. 
//-For asynchronous delivery, a client can register a MessageListener object with a JMSConsumer.
// As messages arrive, the provider delivers them by calling the MessageListener's onMessage().
//	void close(), Message receive(), Message receive(long timeout), <T> T receiveBody(Class<T> c), Message receiveNoWait(),
//	void setMessageListener(MessageListener listener), MessageListener getMessageListener(), String getMessageSelector()


//		Writing Message Producers
//-The new JMS simplified API allows you to write producers and consumers in a less verbose manner than with the classic API.
//-But it still needs both of the administered objects: ConnectionFactory and Destination.
// Depending if you are running outside or inside a container (EJB, Web or ACC) you will either use JNDI lookups or injection.
//-The JMSContext API is the central API to produce and consume messages. 
//-If your app runs outside a container you will need to manage the lifecycle of the JMSContext (by creating, closing it programmatically).
//-If you run inside a container you can just inject it and leave the container to manage its lifecycle.

//	Producing a Message outside a Container
//-A message producer (JMSProducer) is an object created by the JMSContext and is used to send messages to a destination.
//-The following steps explain how to create a producer that sends a message to a queue outside any container (in Java SE env):
// • Obtain a connection factory and a queue using JNDI lookups
// • Create a JMSContext object using the connection factory (notice the try-with-resources that will auto close the JMSContext object)
// • Create a javax.jms.JMSProducer using the JSMContext object
// • Send a text message to the queue using the JMSProducer.send() method
//	public class Producer {
//	    public static void main(String[] args) {
//	        try {
//	            // Looks up the administered objects
//	            ConnectionFactory connectionFactory = InitialContext.doLookup("jms/javaee7/ConnectionFactory");
//	            Destination queue = InitialContext.doLookup("jms/javaee7/Queue");
//	            // Sends a text message to the queue
//	            try (JMSContext context = connectionFactory.createContext()) {
//	                context.createProducer().send(queue, "Text message sent at " + new Date());
//	            }
//	        } catch (NamingException e) {
//	            e.printStackTrace();
//	        }
//	    }
//-If you compare the code with the one using the classic API, you will notice that the code is less verbose.
//-Exception handling is also neater as the new JMSRuntimeException is used in the new API and is an unchecked exception.

//	Producing a Message inside a Container
//-When the client code runs inside a container, dependency injection can be used instead JNDI look up.
//-Java EE 7 has several containers: EJB, servlet, and application client container (ACC). If the code runs in one of these
// containers, the @Resource can be used to inject a reference to that resource by the container. Using resources is much easier,
// as you don’t have the complexity of JNDI or are not required to configure resource references in DDs.
// You just rely on the container injection capabilities.
//-When the ProducerEJB runs in a container, references of ConnectionFactory and Queue are injected at initialization.
//	@Stateless
//	public class ProducerEJB {
//	    @Resource(lookup = "jms/javaee7/ConnectionFactory")
//	    private ConnectionFactory connectionFactory;
//	    @Resource(lookup = "jms/javaee7/Queue")
//	    private Queue queue;
//	    public void sendMessage() {
//	        try (JMSContext context = connectionFactory.createContext()) {
//	            context.createProducer().send(queue, "Text message sent at " + new Date());
//	        }
//	    }
//	}
//-Code is simpler, it doesn’t deal with JNDI lookups or NamingException. The container injects the objects once the EJB is initialized.

//	Producing a Message inside a Container with CDI
//-When the producer is executed in a container (EJB or Servlet container) with CDI enabled, it can inject the JMSContext. 
//-The container will then manage its lifecycle (no need to create or close the JMSContext).
//-This can be done thanks to the @Inject and @JMSConnectionFactory annotations.
//-@JMSConnectionFactory may be used to specify the JNDI lookup name of the ConnectionFactory used to create the JMSContext.
//-If the @JMSConnectionFactory is omitted, then the platform default JMS connection factory will be used.
//	public class Producer {
//	    @Inject
//	    @JMSConnectionFactory("jms/javaee7/ConnectionFactory")
//	    private JMSContext context;
//	    @Resource(lookup = "jms/javaee7/Queue")
//	    private Queue queue;
//	    public void sendMessage() {
//	        context.createProducer().send(queue, "Text message sent at " + new Date());
//	    }
//	}
//-The code is quite minimalist. The container does all the work of injecting the needed components and managing their lifecycle.
//-As a developer you just need one line of code to send a message.
//-The @JMSPasswordCredential can also be used to specify a user name and password for when the JMSContext is created:
//	@Inject
//	@JMSConnectionFactory("jms/connectionFactory")
//	@JMSPasswordCredential(userName="admin",password="mypassword")
//	private JMSContext context;


//		Writing Message Consumers
//-A client uses a JMSConsumer to receive messages from a destination. 
//-A JMSConsumer is created by passing a Queue or Topic to the JMSContext.createConsumer(). 
//-Messaging is inherently asynchronous, in that there is no timing dependency between producers and consumers.
// However, the client itself can consume messages in two ways:
// • Synchronously: A receiver explicitly fetches the message from the destination by calling the receive() method.
// • Asynchronously: A receiver decides to register to an event that is triggered when the message arrives. It has to implement
// the MessageListener interface, and, whenever a message arrives, the provider delivers it by calling the onMessage().

//	Synchronous Delivery
//-A sync consumer needs to start a JMSContext, loop to wait until a new message arrives, and request the arrived message using
// one of its receive() methods. There are several variations of receive() that allow a client to pull or wait for the next message.
//-The following steps explain how you can create a synchronous consumer that consumes a message from a queue:
// • Obtain a connection factory and a topic using JNDI lookups (or injection)
// • Create a JMSContext object using the connection factory
// • Create a javax.jms.JMSConsumer using the JSMContext object
// • Loop and call the receive() method (or in this case receiveBody) on the consumer object. 
//-The receive() methods block if the queue is empty and wait for a message to arrive. Here, the infinite loop waits for messages to arrive
//	    public static void main(String[] args) {
//	        try {
//	            ConnectionFactory connectionFactory = InitialContext.doLookup("jms/javaee7/ConnectionFactory");
//	            Destination queue = InitialContext.doLookup("jms/javaee7/Queue");
//	            // Loops to receive the messages
//	            try (JMSContext context = connectionFactory.createContext()) {
//	                while (true) {
//	                    String message = context.createConsumer(queue).receiveBody(String.class);
//	                }
///	            }
//	        } catch (NamingException e) {
//	            e.printStackTrace();
//	        }
//	    }
//-Again, if you compare the code with the one using the classic API, you will see how the new simplified API is easier to use and more expressive.

//	Asynchronous Delivery
//-Asynchronous consumption is based on event handling. A client can register an object that implements the MessageListener intf.
//-A message listener is an object that acts as an asynchronous event handler for messages. 
//-As messages arrive, the provider delivers them by calling the listener’s onMessage() , which takes one argument of type Message.
//-With this event model, the consumer doesn’t need to loop indefinitely to receive a message.
//-MDBs use this event model.

//-The following steps describe the process used to create an asynchronous message listener:
// • The class implements the javax.jms.MessageListener interface, which defines a single method called onMessage()
// • Obtain a connection factory and a topic using JNDI lookups (or injection)
// • Create a javax.jms.JMSConsumer using the JSMContext object
// • Call the setMessageListener() method, passing an instance of a MessageListener interface
// • Implement the onMessage() and process the received message. Each time a message arrives, the provider will invoke this method, passing the message
//	public class Listener implements MessageListener {
//	    public static void main(String[] args) {
//	        try {
//	            // Looks up the administered objects
//	            ConnectionFactory connectionFactory = InitialContext.doLookup("jms/javaee7/ConnectionFactory");
//	            Destination queue = InitialContext.doLookup("jms/javaee7/Queue");
//	            try (JMSContext context = connectionFactory.createContext()) {
//	                context.createConsumer(queue).setMessageListener(new Listener());
//	            }
//	        } catch (NamingException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	    public void onMessage(Message message) {
//	        System.out.println("Async Message received: " + message.getBody(String.class));
//	    }
//	}

public class Info_JMS_Simplified_API {}
