package client_standalone;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

//Doesnt work this way
//http://techtipsjava.blogspot.com/2013/05/jms-on-glassfish-queue-and-topic-with.html

@SuppressWarnings("unused")
public class JMSTestSEPropertiesConn {
	
	private String providerUrl = "mq://localhost:7676";
	private String connectionFactory = "jms/__defaultConnectionFactory";
//	private String queueSimple = "java:global/jms/QueueSimple";
	private String queueSimple = "jms/__defaultQueue";
	
	private void sendMessageToQueue(String msg) throws Exception {
		
		// Provide the details of remote JMS Client
		Properties props = new Properties();
		//props.put(Context.PROVIDER_URL, providerUrl);
		
		//props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        //props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
        //props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

		//System.setProperty("java.security.auth.login.config", "c:/JavaServers/payara5/glassfish/lib/appclient/appclientlogin.conf");
		//props.setProperty("org.omg.CORBA.ORBInitialHost", "10.1.1.13"); // default!
		//props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost"); // default!
		//props.setProperty("org.omg.CORBA.ORBInitialPort", "3700"); // default!
		
//		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
//		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
//		props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");

		// Create the initial context for remote JMS server
		InitialContext ctx = new InitialContext(props);
		System.out.println("Context Created.");

		// JNDI Lookup for QueueConnectionFactory in remote JMS Provider
		QueueConnectionFactory qFactory = (QueueConnectionFactory) ctx.lookup(connectionFactory);

		// Create a Connection from QueueConnectionFactory
		Connection connection = qFactory.createConnection();
		System.out.println("Connection established.");

		// Initialise the communication session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// Create the message
		TextMessage message = session.createTextMessage();
		message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
		message.setText(msg);
		message.setIntProperty("consumer", 1);
		
		// JNDI Lookup for the Queue in remote JMS Provider
		Queue queue = (Queue) ctx.lookup(queueSimple);

		// Create the MessageProducer for this communication
		// Session on the Queue we have
		MessageProducer mp = session.createProducer(queue);

		// Send the message to Queue
		mp.send(message);
		System.out.println("Message Sent: " + msg);

		// Make sure all the resources are released
		mp.close();
		session.close();
		ctx.close();

	}
	
	
	public static void main(String[] args) throws Exception {

		JMSTestSEPropertiesConn test = new JMSTestSEPropertiesConn();
		System.out.println("---- Sending message to Queue ----");

		String msg = "Remote JMS Client Message";
		test.sendMessageToQueue(msg);
		
		System.exit(0);
	}
	
}