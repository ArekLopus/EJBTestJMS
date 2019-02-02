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

//	 Message Time-to-Live
//-Under heavy load, a time-to-live can be set on messages to ensure that the provider will remove them from the destination
// when they become obsolete, by either using the JMSProducer API or setting the JMSExpiration header field.
//-The JMSProducer has a setTimeToLive() that takes a number of milliseconds:
//	context.createProducer().setTimeToLive(1000).send(queue, message);

@WebServlet("/jmsMessageTimeToLive")
public class MessageTimeToLiveProducerServlet extends HttpServlet {
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
			
			producer.setTimeToLive(1000).send(queue, message);
			
			System.out.println("Message sent: " + msg);

			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		
		
		
		out.println("");
	}
	
}
