package jms.reliability_mechanisms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//	Creating Durable Consumers (Topics)
//-The disadvantage of using the pub-sub model is that a message consumer must be running when the messages are sent to the topic;
// otherwise, it will not receive them. By using durable consumers, the JMS API provides a way to keep messages in the topic
// until all subscribed consumers receive them. With durable subscription, the consumer can be offline for some time, but,
// when it reconnects, it receives the messages that arrived during its disconnection.
//-To achieve this, the client creates a durable consumer using the JMSContext:
//	context.createDurableConsumer(topic, "javaee7DurableSubscription").receive();

//-At this point, the client program starts the connection and receives messages. The name javaee7DurableSubscription is used
// as an identifier of the durable subscription. Each durable consumer must have a unique ID,
// resulting in the declaration of a unique connection factory for each potential, durable consumer.

@WebServlet("/jmsDurableConsumers")
public class DurableConsumersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
    @JMSConnectionFactory("jms/__defaultConnectionFactory")
    private JMSContext ctx;
	
	@Resource(lookup = "jms/__defaultTopic")
    private Topic topic;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JMS - Durable Consumers</h3>");
		out.flush();
		
		JMSConsumer consumer = ctx.createDurableConsumer(topic, "AnIdentifierOfTheDurableSubscription");
		
		while (true) {
            String message = consumer.receiveBody(String.class);
            System.out.println("Filtering messages (number > 5) Consumer, received: " + message);
        }
		
	}
	
}
