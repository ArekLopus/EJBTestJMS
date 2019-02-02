package client_standalone;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class JMSTestSE {
	
	private String connectionFactory = "jms/__defaultConnectionFactory";
	private String queueSimple = "java:global/jms/QueueSimple";
	
	private void sendMessageToQueue(String msg) throws Exception {
		
		// Create the initial context for remote JMS server
		InitialContext ctx = new InitialContext();
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

		JMSTestSE test = new JMSTestSE();
		System.out.println("---- Sending message to Queue ----");
		
		String msg = "Remote JMS Client Message";
		test.sendMessageToQueue(msg);
		
		System.exit(0);
	}
	
}