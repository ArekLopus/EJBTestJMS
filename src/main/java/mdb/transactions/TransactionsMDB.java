package mdb.transactions;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.transaction.TransactionSynchronizationRegistry;

//	Transactions
//-MDBs can use BMTs or container-managed transactions (CMTs); they can explicitly roll back a transaction by using
// the MessageDrivenContext.setRollbackOnly() and so on. 
//-The container will start a transaction before the onMessage() is invoked and will commit the transaction
// when the method returns (unless the transaction was marked for rollback with setRollbackOnly()).
//-Even though MDBs are transactional, they cannot execute in the client’s transaction context, as they don’t have a client.
// Nobody explicitly invokes methods on MDBs, they just listen to a destination and consume messages. There is no context passed
// from a client to an MDB, and similarly the client transaction context cannot be passed to the onMessage().

//-In CMTs, MDBs can use the @TransactionAttribute on business methods with the two following attributes:
// • REQUIRED (the default): If the MDB invokes other enterprise beans, the container passes the transaction context with
//   the invocation. The container attempts to commit the transaction when the message listener method has completed
// • NOT_SUPPORTED: If the MDB invokes other enterprise beans, the container passes no transaction context with the invocation

//@TransactionManagement(TransactionManagementType.BEAN)
@TransactionManagement(TransactionManagementType.CONTAINER)
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/jms/QueueTest"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "Transactions = 'tr'")
})
public class TransactionsMDB implements MessageListener {
	
	@Resource
	TransactionSynchronizationRegistry tsr;
	
	@Override
	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		try {
			System.out.println("Transactions MDB works, message: " + msg.getBody(String.class) + ", trans: " + tsr.getTransactionKey());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}


}
