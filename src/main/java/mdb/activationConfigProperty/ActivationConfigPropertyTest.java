package mdb.activationConfigProperty;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

//	@ActivationConfigProperty
//-JMS allows configuration of certain properties such as message selectors, acknowledgment mode, durable subscribers, and so on.
//-This optional annotation can be provided as one of the parameters for the @MessageDriven annotation.
//-The @ActivationConfigProperty is very basic, consisting of a name-value pair.
//-The activationConfig property allows you to provide standard and nonstandard (provider–specific) configuration.
//-List of some standard properties you can use.
//	Property 					Description
//	acknowledgeMode 			The acknowledgment mode (default is AUTO_ACKNOWLEDGE)
//	messageSelector 			The message selector string used by the MDB
//	destinationType 			The destination type, which can be TOPIC or QUEUE
//	destinationLookup 			The lookup name of an administratively-defined Queue or Topic
//	connectionFactoryLookup 	The lookup name of an administratively defined ConnectionFactory
//	destination 				The name of the destination.
//	subscriptionDurability 		The subscription durability (default is NON_DURABLE)
//	subscriptionName 			The subscription name of the consumer
//	shareSubscriptions 			Used if the message-driven bean is deployed into a clustered
//	clientId 					Client identifier that will be used when connecting to the JMS provider


@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/jms/QueueTest"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "ActivationConfigProperty = 'acp'")
})
public class ActivationConfigPropertyTest implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		try {
			System.out.println("ActivationConfigProperty MDB works, message: " + msg.getBody(String.class));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}


}
