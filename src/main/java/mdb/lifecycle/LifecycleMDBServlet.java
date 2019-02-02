package mdb.lifecycle;

import java.io.IOException;
import java.io.PrintWriter;

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

@WebServlet("/mdbLifecycle")
public class LifecycleMDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	@JMSConnectionFactory("java:global/jms/testConnectionFactory")
	private JMSContext ctx;
	    
	@Resource(lookup = "java:global/jms/QueueTest")
	private Queue queue;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>MDB - Lifecycle </h3>");
		out.println("The MDB life cycle is identical to that of the stateless session bean: either the MDB exists and is ready to consume messages or it doesn't exist.");
		
		try {
			
			String msg = "Text message, Lifecycle";
			
			TextMessage message = ctx.createTextMessage(msg);
			message.setStringProperty("Lifecycle", "lc");
			
			JMSProducer producer = ctx.createProducer();
			
			producer.send(queue, message);
			
			System.out.println("Message sent: " + msg);
						
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
	
}
