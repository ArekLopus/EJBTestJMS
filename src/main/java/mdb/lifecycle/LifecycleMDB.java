package mdb.lifecycle;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.interceptor.Interceptors;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

//-The MDB life cycle is identical to that of the stateless session bean:
// either the MDB exists and is ready to consume messages or it doesn’t exist. 
//-Before exiting, the container first creates an instance of the MDB and, if applicable, injects the necessary resources
// as specified by metadata annotations (@Resource, @Inject, @EJB, etc.) or DD. 
//-The container then calls the bean’s @PostConstruct callback method, if any.
//-After this, the MDB is in the ready state and waits to consume any incoming message.
//-The @PreDestroy callback occurs when the MDB is removed from the pool or destroyed.

//-This behavior is identical to that of stateless session beans (see Ch8 for more details about callback methods),
// and, like other EJBs, you can add interceptors.

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/jms/QueueTest"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "Lifecycle = 'lc'")
})
@Interceptors(LifecycleInterceptors.class)
public class LifecycleMDB implements MessageListener {
	
	public LifecycleMDB() {
		System.out.println("LifecycleMDB's constructor");
	}
	
	@Override
	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		try {
			System.out.println("Lifecycle MDB works, message: " + msg.getBody(String.class));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}


}
