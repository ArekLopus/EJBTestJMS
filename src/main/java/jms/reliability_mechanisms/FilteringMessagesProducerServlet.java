package jms.reliability_mechanisms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jmsFilteringProducer")
public class FilteringMessagesProducerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Inject
    @JMSConnectionFactory("jms/__defaultConnectionFactory")
	//@JMSPasswordCredential(userName="admin",password="admin")
    private JMSContext ctx;
	
	@Resource(lookup = "jms/__defaultQueue")
    private Queue queue;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS - Filtering Messages Producer</h3>");
		
		try {
			int nextInt = ThreadLocalRandom.current().nextInt(10) +1;
			String msg = "Text message, number property: " + nextInt;
			
			TextMessage message = ctx.createTextMessage(msg);
			message.setIntProperty("number", nextInt);
			
			ctx.createProducer().send(queue, message);
			System.out.println("Message sent: " + msg);

			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		
		
		
		out.println("");
	}
	
}
