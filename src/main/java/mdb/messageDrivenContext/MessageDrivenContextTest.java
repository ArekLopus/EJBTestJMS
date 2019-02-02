package mdb.messageDrivenContext;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.transaction.TransactionSynchronizationRegistry;

//MDB Context
//-The MessageDrivenContext interface provides access to the runtime context that the container provides for an MDB instance.
//-The container passes the MessageDrivenContext interface to this instance, which remains associated for the lifetime of the MDB.
// This gives the MDB the possibility to explicitly roll back a transaction, get the user principal, and so on.
//-The MessageDrivenContext interface extends the javax.ejb.EJBContext interface without adding any extra methods. 

//    Method 				Description
//    getCallerPrincipal 	Returns the java.security.Principal associated with the invocation
//    getRollbackOnly 		Tests whether the current transaction has been marked for rollback
//    getTimerService 		Returns the javax.ejb.TimerService interface
//    getUserTransaction 	Returns the javax.transaction.UserTransaction interface to use to demarcate transactions.
//							Only MDBs with bean-managed transaction (BMT) can use this method
//    isCallerInRole 		Tests whether the caller has a given security role
//    Lookup 				Enables the MDB to look up its environment entries in the JNDI naming context
//    setRollbackOnly 		Marks the current transaction as rollback. Only MDBs with BMT can use this method


@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/jms/QueueTest"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "MessageDrivenContext = 'mdc'")
})
public class MessageDrivenContextTest implements MessageListener {
	
	@Resource
	MessageDrivenContext ctx;
	
	@Resource
	TransactionSynchronizationRegistry tsr;
	
	@Override
	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		try {
			//ctx.setRollbackOnly();	//tries to send it again because message is persistent and transactions fails
			System.out.println("Trans: " + tsr.getTransactionKey());
			System.out.println("Principal: " + ctx.getCallerPrincipal());
			System.out.println("Is Rollback: " + ctx.getRollbackOnly());
			
			System.out.println("MessageDrivenContext MDB works, message: " + msg.getBody(String.class));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}


}
