package mdb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

//	MDB as a Producer
//-MDBs are capable of becoming message producers, something that occurs when they are involved in a workflow, as they receive a
// message from one destination, process it, and send it to another destination. To add this capability, the JMS API must be used.

//-A destination and a connection factory can be injected by using the @Resource and @JMSConnectionFactory annotations
// or via JNDI lookup, and then methods on the javax.jms.JMSContext object can be invoked to create and send a message. 

@MessageDriven(mappedName = "jms/__defaultQueue", activationConfig = {
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "producer = 1")
})
public class MDBProducer implements MessageListener {
	
	@Inject
    @JMSConnectionFactory("jms/__defaultConnectionFactory")
    private JMSContext ctx;

    @Resource(lookup = "jms/__defaultQueue")
    private Destination queue;
	
	@Override
	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		
		try {
			String body = msg.getBody(String.class);
			System.out.println("MDB Producer Works as a Consumer: " + body);
			
			TextMessage newMessage = ctx.createTextMessage("Message resent by MDBProducer: " + body);
			newMessage.setIntProperty("consumer", 1);
			
			JMSProducer producer = ctx.createProducer();
			producer.send(queue, newMessage);
			
			System.out.println("Message resent: " + newMessage.getBody(String.class));
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}


}
