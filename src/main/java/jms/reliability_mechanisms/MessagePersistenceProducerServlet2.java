package jms.reliability_mechanisms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.DeliveryMode;
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

//https://stackoverflow.com/questions/43459812/difference-between-persistent-and-non-persistent-delivery

//	Specifying Message Persistence
//-JMS supports two modes of message delivery:		persistent and nonpersistent. 
//-Persistent delivery (defaulr) ensures that a message is delivered only once to a consumer, 
//-Nonpersistent delivery requires a message be delivered once at most.
//-Persistent delivery is more reliable, but at a performance cost, as it prevents losing a message if a provider failure occurs.
//-The delivery mode can be specified by using the setDeliveryMode() of the JMSProducer interface:
//	context.createProducer().setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(queue, message);

@WebServlet("/jmsMessagePersistence")
public class MessagePersistenceProducerServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Inject
    @JMSConnectionFactory("jms/__defaultConnectionFactory")
    private JMSContext ctx;
	
	@Resource(lookup = "jms/__defaultQueue")
    private Queue queue;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS - Message Persistence</h3>");
		
		try {
			int nextInt = ThreadLocalRandom.current().nextInt(10) +1;
			String msg = "Text message, number property: " + nextInt;
			
			TextMessage message = ctx.createTextMessage(msg);
			message.setIntProperty("number", nextInt);
			
			JMSProducer producer = ctx.createProducer();
			
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(queue, message);
			
			System.out.println("Message sent: " + msg);

			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		
		
		
		out.println("");
	}
	
}
