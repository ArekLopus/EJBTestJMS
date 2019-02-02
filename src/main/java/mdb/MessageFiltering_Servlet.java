package mdb;

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
import javax.jms.Topic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/mdbMessageFiltering")
public class MessageFiltering_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	@JMSConnectionFactory("java:global/jms/testConnectionFactory")
	private JMSContext ctx;
	    
	@Resource(lookup = "java:global/jms/TopicTest")
	private Topic topic;
	
	@Resource(lookup = "java:global/jms/FilteringMessagesQueue")
	private Queue queue;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>MDB - Filtering Messages Producer</h3>");
		
		try {
			int nextInt = ThreadLocalRandom.current().nextInt(10) +1;
			String msg = "Text message, number property: " + nextInt;
			
			TextMessage message = ctx.createTextMessage(msg);
			message.setIntProperty("number", nextInt);
			
			JMSProducer producer = ctx.createProducer();
			
			producer.send(queue, message);
			producer.send(topic, message);
			
			System.out.println("Message sent: " + msg);
						
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
