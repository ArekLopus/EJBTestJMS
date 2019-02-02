package jms.api_simplified;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//	setMessageListener()		- Only for Java SE!
//-This method must not be used in a Java EE web or EJB application.
// Doing so may cause a JMSRuntimeException to be thrown though this is not guaranteed.
//2019-01-02T14:18:30.604+0100|Warning: StandardWrapperValve[jms.simplified.SimplifiedAPIConsumerAsyncServlet]:
//	Servlet.service() for servlet jms.simplified.SimplifiedAPIConsumerAsyncServlet threw exception
//	javax.jms.JMSRuntimeException: [C4306]: This method may not be called in a Java EE web or EJB container
@WebServlet("/jmsSimplifiedConsumerListener")
public class SimplifiedAPIConsumerAsyncServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
//	@Inject
//    @JMSConnectionFactory("jms/__defaultConnectionFactory")
//    private JMSContext context;
//	
//	@Resource(lookup = "jms/__defaultQueue")
//    private Queue queue;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS Simplified API Consumer Asynchronous</h3>");
		out.flush();
		
		//2019-01-02T14:18:30.604+0100|Warning: StandardWrapperValve[jms.simplified.SimplifiedAPIConsumerAsyncServlet]: Servlet.service() for servlet jms.simplified.SimplifiedAPIConsumerAsyncServlet threw exception
		//	javax.jms.JMSRuntimeException: [C4306]: This method may not be called in a Java EE web or EJB container
		try {
            // Looks up the administered objects
            ConnectionFactory connectionFactory = InitialContext.doLookup("jms/__defaultConnectionFactory");
            Destination queue = InitialContext.doLookup("jms/__defaultQueue");
            try (JMSContext context = connectionFactory.createContext()) {
                context.createConsumer(queue).setMessageListener(m -> {
        			try {
        				System.out.println("Simplified Consumer Async, received: " + m.getBody(String.class) + ", thread: " + Thread.currentThread().getName());
        			} catch (JMSException e) {
        				e.printStackTrace();
        			}
                });
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
		
		
		//javax.jms.JMSRuntimeException: [C4306]: This method may not be called in a Java EE web or EJB container
		//	at com.sun.messaging.jmq.jmsclient.JMSConsumerImpl.setMessageListener(JMSConsumerImpl.java:261)
//		context.createConsumer(queue).setMessageListener(m -> {
//			try {
//				System.out.println("Simplified Message Consumer Asynchronous (listener), received: " + m.getBody(String.class) + ", thread: " + Thread.currentThread().getName());
//			} catch (JMSException e) {
//				e.printStackTrace();
//			}
//		});
		
		
		out.println("");
	}
	
}
