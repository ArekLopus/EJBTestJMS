package destination_definition;

import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

//-MDB for Topic works just with mappedName. 

@MessageDriven(mappedName="java:global/jms/TopicSimple")
public class DestinationDefinitionTopic implements MessageListener {

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
