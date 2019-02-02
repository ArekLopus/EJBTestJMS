package jms.api_classic;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jmsClassicProducer")
public class ClassicAPIProducerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS - Classic API Producer</h3>");
		
		try {
			ConnectionFactory cf = InitialContext.doLookup("jms/__defaultConnectionFactory");
			Queue queue = InitialContext.doLookup("jms/__defaultQueue");
			
			System.out.println(cf);
			System.out.println(queue);
			
			Connection conn = cf.createConnection();
			Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(queue);
			
			TextMessage message = session.createTextMessage("Text message sent at " + new Date());
			messageProducer.send(message);
			System.out.println("Message sent: " + message);
			
			conn.close();
			
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
		
		
		out.println("");
	}
	
}
