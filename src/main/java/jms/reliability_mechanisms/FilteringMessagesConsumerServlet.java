package jms.reliability_mechanisms;

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

//	Filtering messages
//-Some messaging applications need to filter the messages they receive.
//-When a message is broadcast to many clients, it becomes useful to set criteria so that it is only consumed by certain receivers. 
 //This eliminates both the time and bandwidth the provider would waste transporting messages to clients that don’t need them.
//-Messages are composed of 3 parts: header, properties, and body.
// The header contains a fixed number of fields (the message metadata), and the properties are a set of custom name-value pairs
// that the application can use to set any values.
//-Selection can be done on those two areas:
// Producers set one or several property values or header fields, and
// the consumer specifies message selection criteria using selector expressions. 
//-Only messages that match the selector are delivered.
//-Message selectors assign the work of filtering messages to the JMS provider, rather than to the application.
//-A message selector is a string that contains an expression.
//-The syntax of the expression is based on a subset of the SQL92 conditional expression syntax and looks like this:
//	context.createConsumer(queue, "JMSPriority < 6").receive();
//	context.createConsumer(queue, "JMSPriority < 6 AND orderAmount < 200").receive();
//	context.createConsumer(queue, "orderAmount BETWEEN 1000 AND 2000").receive();
//-In the preceding code, a consumer is created with the JMSContext.createConsumer(), passing a selector string.
// This string can use header fields (JMSPriority < 6) or custom properties (orderAmount < 200).
//-The producer sets these properties into the message:
//	context.createTextMessage().setIntProperty("orderAmount", 1530);
//	context.createTextMessage().setJMSPriority(5);
//-Selector expression can use logical operators (NOT, AND, OR), comparison operators (=, >, >=, <, <=, <>),
// arithmetic operators (+, -, *, /), expressions ([NOT] BETWEEN, [NOT] IN, [NOT] LIKE, IS [NOT] NULL), and so on.

@WebServlet("/jmsFilteringConsumer")
public class FilteringMessagesConsumerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
    @JMSConnectionFactory("jms/__defaultConnectionFactory")
    private JMSContext ctx;
	
	@Resource(lookup = "jms/__defaultQueue")
    private Queue queue;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS - Filtering Messages Producer</h3>");
		out.flush();
		
		//JMSConsumer consumer = ctx.createConsumer(queue);
		JMSConsumer consumer = ctx.createConsumer(queue, "number > 5");
		
		while (true) {
            String message = consumer.receiveBody(String.class);
            System.out.println("Filtering messages (number > 5) Consumer, received: " + message);
        }
		
	}
	
}
