package destination_definition;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/mdbDestinationDefinitionTopic")
public class DestinationDefinitionTopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	@JMSConnectionFactory("jms/__defaultConnectionFactory")
	private JMSContext ctx;
	    
	@Resource(lookup = "java:global/jms/TopicSimple")
	private Topic topic;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>MDB - Destination Definition Topic</h3>");
		
		
		JMSProducer producer = ctx.createProducer();
		producer.send(topic, "Just a simple String message for Topic");
		System.out.println("Message sent.");
		
	}
	
}
