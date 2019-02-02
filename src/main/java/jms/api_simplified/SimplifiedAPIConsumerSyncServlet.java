package jms.api_simplified;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jmsSimplifiedConsumerLoop")
public class SimplifiedAPIConsumerSyncServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
    @JMSConnectionFactory("jms/__defaultConnectionFactory")
    private JMSContext context;
	
	@Resource(lookup = "jms/__defaultQueue")
    private Queue queue;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS Simplified API Consumer Synchronous</h3>");
		out.flush();
		
		JMSConsumer consumer = context.createConsumer(queue);
		
		while (true) {
            String message = consumer.receiveBody(String.class);
            System.out.println("Simplified Consumer Sync, received: " + message + ", thread: " + Thread.currentThread().getName());
        }
		
//		while (true) {
//            String message = context.createConsumer(queue).receiveBody(String.class);
//            System.out.println("Message received: " + message + ", thread: " + Thread.currentThread().getName());
//        }
		
	}
	
}
