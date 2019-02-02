package mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

//@MessageDriven(mappedName = "java:app/jms/FilteringMessagesQueue")
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/jms/FilteringMessagesQueue"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "number > 5")
})
public class MessageFilteringQueueMDB implements MessageListener {

	@Override
	public void onMessage(Message message) {
		
		try {
			TextMessage mes = (TextMessage) message;
			String body = mes.getBody(String.class);
			
			System.out.println("MDB Queue with @ActivationConfigProperty name > 5: " + body);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
}