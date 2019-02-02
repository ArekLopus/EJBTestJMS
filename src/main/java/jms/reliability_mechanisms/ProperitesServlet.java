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

//	Setting Priorities
//-You can use message priority levels to instruct the JMS provider to deliver urgent messages first. JMS defines ten priority
// values, with 0 as the lowest and 9 as the highest. You can specify the priority value by using the setPriority() of the JMSProducer:
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


@WebServlet("/jmsProperties")
public class ProperitesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Inject
    @JMSConnectionFactory("jms/__defaultConnectionFactory")
    private JMSContext ctx;
	
	@Resource(lookup = "jms/__defaultQueue")
    private Queue queue;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS - Message Time-To-Live</h3>");
		
		try {
			int nextInt = ThreadLocalRandom.current().nextInt(10) +1;
			String msg = "Text message, number property: " + nextInt;
			
			TextMessage message = ctx.createTextMessage(msg);
			message.setIntProperty("number", nextInt);
			
			JMSProducer producer = ctx.createProducer();
			
			producer
				.setPriority(5)
			    .setTimeToLive(1000)
			    .setDeliveryMode(DeliveryMode.NON_PERSISTENT)
				.send(queue, message);
			
			System.out.println("Message sent: " + msg);

			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
	
}
