package mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

//	MDB as a Consumer
//-Consumers can receive a message either synchronously, by looping and waiting for a message to arrive,
// or asynchronously, by implementing the MessageListener interface. 
//-By nature, MDBs are designed to function as asynchronous message consumers. MDBs implement a message listener interface,
// which is triggered by the container when a message arrives.
//-Can an MDB be a synchronous consumer? Yes, but this is not recommended.
// Synchronous message consumers block and tie up server resources (the EJBs will be stuck looping without performing any work,
// and the container will not be able to free them).
//-MDBs, like SL SBs, live in a pool of a certain size. When the container needs an instance, it takes 1 out of the pool and uses it.
// If each instance goes into an infinite loop, the pool will eventually empty, and all the available instances will be busy
// looping. The EJB container can also start generating more MDB instances, growing the pool and eating up more and more memory.
//-For this reason, session beans and MDBs should not be used as synchronous message consumers.

//	Enterprise Beans		Producer		Synchronous Consumer		Asynchronous Consumer
//	Session beans			Yes				Not recommended				Not possible
//	MDB						Yes				Not recommended				Yes

@MessageDriven(mappedName = "jms/__defaultQueue", activationConfig = {
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "consumer = 1")
})
public class MDBConsumer implements MessageListener {
	
	@Override
	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		try {
			System.out.println("MDB Consumer works: " + msg.getBody(String.class));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}


}
