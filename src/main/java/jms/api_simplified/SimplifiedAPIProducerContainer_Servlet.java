package jms.api_simplified;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jmsSimplifiedProducerContainer")
public class SimplifiedAPIProducerContainer_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(lookup = "jms/__defaultConnectionFactory")
    private ConnectionFactory cf;
    @Resource(lookup = "jms/__defaultQueue")
    private Queue queue;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS - Simplified API Producer - Container</h3>");

		try (JMSContext context = cf.createContext()) {
			String message = "Text message, Container Producer, sent at " + new Date();
			context.createProducer().send(queue, message);
			System.out.println("Message sent: " + message);
		}
			
		out.println("");
	}
	
}
