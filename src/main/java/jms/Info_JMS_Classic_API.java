package jms;

//	Classic API
//-This API enables asynchronous communication between clients by providing a connection to the provider,
// and a session where messages can be created and sent or received.

//	ConnectionFactory
//-Connection factories are administered objs that allow an app to connect to a provider by creating a Connection obj programmatically.
//-A ConnectionFactory is an interface that encapsulates the configuration parameters that have been defined by an administrator.
//-To use an administered object such as a ConnectionFactory, the client needs to perform a JNDI lookup (or use injection).
// Fe, the following code fragment obtains the JNDI InitialContext object and uses it to look up a ConnectionFactory by its JNDI name:
//	Context ctx = new InitialContext();
//	ConnectionFactory ConnectionFactory = (ConnectionFactory) ctx.lookup("jms/javaee7/ConnectionFactory");
//-The methods available in this interface are createConnection methods that return a Connection obj
// and new JMS 2.0 createContext methods that return a JMSContext. You can create a Connection or a JMSContext either
// with the default user identity or by specifying a username and password.

//	Destination
//-A destination is an administered object that contains provider-specific configuration info such as the destination address.
//-But this configuration is hidden from the JMS client by using the standard javax.jms.Destination interface. 
//-Like the connection factory, a JNDI lookup is needed to return such objects:
//	Context ctx = new InitialContext();
//	Destination queue = (Destination) ctx.lookup("jms/javaee7/Queue");

//	Connection
//-The Connection object, created using the createConnection() of the connection factory, is a connection to the JMS provider. 
//-Connections are thread-safe and designed to be shareable, as opening a new connection is resource intensive. 
//-However, a session (javax.jms.Session) provides a single-threaded context for sending and receiving messages, using
// a connection to create one or more sessions. Once you have a connection factory, you can use it to create a connection as follows:
//	Connection connection = connectionFactory.createConnection();
//-Before a receiver can consume messages, it must call the start() method.
//-If you need to stop receiving messages temporarily without closing the connection, you can call the stop() method:
//	connection.start();
//	connection.stop();
//-When the application completes, you need to close any connections created.
// Closing a connection also closes its sessions and its producers or consumers:
//	connection.close();

//	Session
//-You create a session from the connection using the createSession(). A session provides a transactional context in which a set
// of messages to be sent or received are grouped in an atomic unit of work, meaning that, if you send several messages during
// the same session, JMS will ensure that either they all are sent or none. This behavior is set at the creation of the session:
//	Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
//-The first parameter of the method specifies whether or not the session is transactional. In the code, the parameter is set
// to true, meaning that the request for sending messages won’t be realized until either the session’s commit() is called or 
// the session is closed. If the parameter was set to false, the session would not be transactional, and messages would be sent
// as soon as the send() is invoked.
//-The second parameter means that the session automatically acknowledges messages when they have been received successfully. 
//-A session is single-threaded and is used to create messages, producers, and consumers.

//	Messages
//-To communicate, clients exchange messages; one producer will send a message to a destination, and a consumer will receive it. 
//-Messages are objects that encapsulate information and are divided in three parts:
// • A header: contains standard information for identifying and routing the message.
// • Properties: are name-value pairs that the application can set or read.
//   Properties also allow destinations to filter messages based on property values.
// • A body: contains the actual message and can take several formats (text, bytes, object, etc.).
//Header
//-The header has predefined name-value pairs, common to all messages that both clients and providers use to identify and route
//messages. They can be seen as message metadata as they give information about the message. Each field has associated getter and
// setter methods defined in the javax.jms.Message interface. Some header fields are intended to be set by a client, but many are
// set automatically by the send() or the publish() method. Table describes each JMS message header field.
//Properties
//-In addition to the header fields, the javax.jms.Message interface supports property values, which are just like headers,
// but explicitly created by the application, instead of being standard across messages. This provides a mechanism for adding
// optional header fields to a message that a client will choose to receive or not via selectors.
//-Property values can be boolean, byte, short, int, long, float, double, and String. The code to set and get properties looks like this: 
//	message.setFloatProperty("orderAmount", 1245.5f);
//	message.getFloatProperty("orderAmount");
//Body
//-The body of a message is optional, and contains the data to send or receive. 
//-Depending on the interface that you use,  it can contain different formats of data, as listed in Table.
//-It is possible to create your own message format, if you extend the javax.jms.Message interface. 
//-Note that, when a message is received, its body is read-only. 
//-Depending on the message type, you have different methods to access its content.
//A text message will have a getText() and setText(), an object message will have a getObject() and setObject(), and so forth:
//	textMessage.setText("This is a text message");	textMessage.getText();
//	bytesMessage.readByte();
//	objectMessage.getObject();
//-Note that since JMS 2.0, the new method <T> T getBody(Class<T> c) returns the message body as an object of the specified type.

//Interface			Description
//StreamMessage		A message whose body contains a stream of Java primitive values. It is filled and read sequentially.
//MapMessage		A message whose body contains a set of name-value pairs where names are strings and values are Java primitive types.
//TextMessage		A message whose body contains a string (for example, it can contain XML).
//ObjectMessage		A message that contains a serializable object or a collection of serializable objects.
//BytesMessage		A message that contains a stream of bytes.


//Table - Fields Contained in the Header
//Field				Description																					Set By
//JMSDestination	This indicates the destination to which the message is being sent.							send() or publish()
//JMSDeliveryMode	JMS supports two modes of message delivery. PERSISTENT mode instructs the provider			send() or publish()
//					to ensure the message is not lost in transit due to a failure. NON_PERSISTENT mode
//					is the lowest-overhead delivery mode because it does not require the message to be
//					logged to a persistent storage.
//JMSMessageID		This provides a value that uniquely identifies each message sent by a provider.				send() or publish()
//JMSTimestamp		This contains the time a message was handed off to a provider to be sent.					send() or publish()
//JMSCorrelationID	A client can use this field to link one message with another such as linking				Client
//					a response message with its request message.
//JMSReplyTo		This contains the destination where a reply to the message should be sent.					Client
//JMSRedelivered	This Boolean value is set by the provider to indicate whether a message been redelivered.	Provider
//JMSType			This serves as a message type identifier.													Client
//JMSExpiration		When a message is sent, its expiration time is calculated and set based on					send() or publish()
//					the time-to-live value specified on the send() method.
//JMSPriority		JMS defines a 10-level priority value, with 0 as the lowest priority and 9 as the highest.	send() or publish()


//	Sending and Receiving a Message with Classic API
//-JMS employs producers, consumers, and destinations. The producer sends a message to the destination, where the consumer is
// waiting for the message to arrive. Destinations can be of two kinds: queues (for point-to-point communication) and topics 
// (for publish-subscribe communication). In Listing, a producer sends a text message to a queue to which the consumer is listening.

//public class Producer {
//    public static void main(String[] args) {
//        try {
//            // Gets the JNDI context
//            Context jndiContext = new InitialContext();
//
//            // Looks up the administered objects
//            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("jms/javaee7/ConnectionFactory");
//            Destination queue = (Destination) jndiContext.lookup("jms/javaee7/Queue");
//
//            // Creates the needed artifacts to connect to the queue
//            Connection connection = connectionFactory.createConnection();
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            MessageProducer producer = session.createProducer(queue);
//
//           // Sends a text message to the queue
//            TextMessage message = session.createTextMessage("Text message sent at " + new Date());
//            producer.send(message);
//            connection.close();
//
//        } catch (NamingException | JMSException e) {
//            e.printStackTrace();
//        }
//    }
//}
//-Fortunately, once you’ve written this code to send a message, the code to receive it looks almost the same. In fact, the first
// lines of the Consumer class in Listing 13-3 are exactly the same: create a JNDI context, lookup for the connection factory and
// the destination, and then connect. The only differences are that a MessageConsumer is used instead of a MessageProducer,
// and that the receiver enters an infinite loop to listen to the queue (you’ll later see that this loop can be avoided by using
// the more standard message listener). When the message arrives, it is consumed and the content displayed. 
//public class Consumer {
//    public static void main(String[] args) {
//        try {
//            // Gets the JNDI context
//            Context jndiContext = new InitialContext();
//
//            // Looks up the administered objects
//            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("jms/javaee7/ConnectionFactory");
//            Destination queue = (Destination) jndiContext.lookup("jms/javaee7/Queue");
//
//            // Creates the needed artifacts to connect to the queue
//            Connection connection = connectionFactory.createConnection();
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            MessageConsumer consumer = session.createConsumer(queue);
//            connection.start();
//
//            // Loops to receive the messages
//            while (true) {
//                TextMessage message = (TextMessage) consumer.receive();
//                System.out.println("Message received: " + message.getText());
//           }
//
//        } catch (NamingException | JMSException e) {
//            e.printStackTrace();
//        }
//    }
//}

public class Info_JMS_Classic_API {}
