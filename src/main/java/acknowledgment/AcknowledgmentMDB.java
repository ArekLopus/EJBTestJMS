package acknowledgment;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.transaction.TransactionSynchronizationRegistry;

//-In transactional sessions, acknowledgment happens automatically when a transaction is committed.
// If a transaction is rolled back, all consumed messages are redelivered.
//-But in nontransactional sessions, an acknowledgment mode must be specified:
// • AUTO_ACKNOWLEDGE: The session automatically acknowledges the receipt of a message 
// • CLIENT_ACKNOWLEDGE: A client acknowledges a message by explicitly calling the Message.acknowledge() method
// • DUPS_OK_ACKNOWLEDGE: This option instructs the session to lazily acknowledge the delivery of messages. This is likely to result in the delivery of some duplicate messages if the JMS provider fails, so it should be used only by consumers that can tolerate duplicate messages. If the message is redelivered, the provider sets the value of the JMSRedelivered header field to true

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/jms/QueueTest"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "Acknowledgment = 'ac'")
})
public class AcknowledgmentMDB implements MessageListener {
	
	@Resource
	TransactionSynchronizationRegistry tsr;
	
	@Override
	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		try {
			System.out.println("Acknowledgment MDB works, message: " + msg.getBody(String.class));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}


}
