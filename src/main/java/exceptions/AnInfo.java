package exceptions;

//-Exception tested in this project to keep EJBTest project possible to run on all servers (No JPA and JMS there)

//-The EJB 3.2 specification outlines two types of exceptions.
// • Application exceptions: Exceptions related to business logic handled by the EJB. Fe, an app exception might be raised
//   if invalid arguments are passed to a method, the inventory level is too low, or the credit card number is invalid.
//   Throwing an app exception does not automatically result in marking the transaction for rollback.
//   As detailed in Table 9-4, the container doesn’t roll back when checked exceptions (which extend Exception) are thrown,
//   but it does for unchecked exceptions (which extend RuntimeException).
// • System exceptions: Exceptions caused by system-level faults: JNDI errors, JVM errors, failure to acquire a DB conn, and so on.
//   A system exception must be a subclass of a RuntimeException or java.rmi.RemoteException (and therefore a subclass
//   of javax.ejb.EJBException). Throwing a system exception results in marking the transaction for rollback.

//-When an EB throws an application exception, the container does not wrap it in another exception.
//-The client should be able to handle any app exception it receives.

//-Because the EJBException is a subclass of RuntimeException, you do not have to specify it in the throws clause of the method
// declaration. If a system exception is thrown, the EJB container might destroy the bean instance.
//-Therefore, a system exception cannot be handled by the bean's client program, but instead requires intervention by a system admin.

//-Table shows all the possible combinations with application exceptions. The first line of the table could be interpreted as
// “If the app exception extends from Exception and has no @ApplicationException, throwing it will not mark the trans for rollback.”

//Extends from		@ApplicationException	The transaction is marked for rollback
//Exception 		No annotation 			No
//Exception 		rollback = true 		Yes
//Exception 		rollback = false 		No
//RuntimeException 	No annotation 			Yes
//RuntimeException 	rollback = true 		Yes
//RuntimeException 	rollback = false 		No

//-rollback default is false

public class AnInfo {}
