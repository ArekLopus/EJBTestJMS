package jms.api_simplified;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jmsSimplifiedProducerSE")
public class SimplifiedAPIProducerSE_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS - Simplified API Producer - SE</h3>");
		
		try {
			
			ConnectionFactory cf = InitialContext.doLookup("jms/__defaultConnectionFactory");
			Queue queue = InitialContext.doLookup("jms/__defaultQueue");
			
			System.out.println(cf);
			System.out.println(queue);
			
			try (JMSContext context = cf.createContext()) {
				String message = "Text message, SE Producer, sent at " + new Date();
                context.createProducer().send(queue, message);
                System.out.println("Message sent: " + message);
            }
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		
		out.println("");
	}
	
}
