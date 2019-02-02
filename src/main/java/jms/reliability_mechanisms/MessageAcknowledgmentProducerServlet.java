package jms.reliability_mechanisms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

@WebServlet("/jmsMessageAcknowledgment")
public class MessageAcknowledgmentProducerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Inject
    @JMSConnectionFactory("jms/__defaultConnectionFactory")
    private JMSContext ctx;
	
	@Resource(lookup = "jms/__defaultQueue")
    private Queue queue;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS - Message Acknowledgment</h3>");
		
		try {
			int nextInt = ThreadLocalRandom.current().nextInt(10) +1;
			String msg = "Text message, number property: " + nextInt;
			
			TextMessage message = ctx.createTextMessage(msg);
			message.setIntProperty("number", nextInt);
			
			JMSProducer producer = ctx.createProducer();
			
			producer.setTimeToLive(1000).send(queue, message);
			
			System.out.println("Message sent: " + msg);

			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		
		
		
		out.println("");
	}
	
}
