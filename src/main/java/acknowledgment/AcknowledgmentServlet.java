package acknowledgment;

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

@WebServlet("/mdbAcknowledgment")
public class AcknowledgmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	@JMSConnectionFactory("java:global/jms/testConnectionFactory")
	private JMSContext ctx;
	    
	@Resource(lookup = "java:global/jms/QueueTest")
	private Queue queue;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>MDB - Acknowledgment</h3>");
		
		try {
			
			String msg = "Text message, Acknowledgment";
			
			TextMessage message = ctx.createTextMessage(msg);
			message.setStringProperty("Acknowledgment", "ac");
			
			JMSProducer producer = ctx.createProducer();
			
			producer.send(queue, message);
			
			System.out.println("Message sent: " + msg);
						
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
	
}
