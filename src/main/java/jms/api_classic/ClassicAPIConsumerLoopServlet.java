package jms.api_classic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
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

@WebServlet("/jmsClassicReceiver")
public class ClassicAPIConsumerLoopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS Classic API Consumer</h3>");
		out.flush();
		
		try {
			ConnectionFactory cf = InitialContext.doLookup("jms/__defaultConnectionFactory");
			Queue queue = InitialContext.doLookup("jms/__defaultQueue");
			
			Connection conn = cf.createConnection();
			Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer consumer = session.createConsumer(queue);
			
			conn.start();
			
			String param = request.getParameter("stop");
			if(param != null) {
				conn.close();
			}
			
            // Loops to receive the messages
            while (true) {
                TextMessage message = (TextMessage) consumer.receive();
                System.out.println("Message received: " + message.getText());
            }
			
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
		
		
		out.println("");
	}
	
}
