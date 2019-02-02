package destination_definition;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

//-MDB for Queue doesnt work just with mappedName! (@MessageDriven(mappedName = "java:global/jms/QueueTest")) 
//-MUST declare @ActivationConfigProperty for destinationType and destinationLookup OR destinationType and destination

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		//@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/jms/QueueSimple"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "QueueSimple"),
})
public class DestinationDefinitionQueue implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		try {
			System.out.println("MDB works, " + msg.getBody(String.class));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}


}
