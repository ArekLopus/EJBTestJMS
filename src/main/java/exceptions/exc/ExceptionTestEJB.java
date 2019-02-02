package exceptions.exc;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionSynchronizationRegistry;

@Stateless
public class ExceptionTestEJB {
	
	@Resource
	private TransactionSynchronizationRegistry tsr;
	
	@PersistenceContext
	private EntityManager em;
	
	private int counter = 0;
	
	public void  testAppException() throws AppException {
		counter++;
		
		em.persist(new User("John " + counter, "Doe"));
		if(true) throw new AppException("AppException");
	}
	public void  testAppExceptionRollback() throws AppExceptionRollback {
		counter++;
		
		em.persist(new User("John " + counter, "Doe"));
		if(true) throw new AppExceptionRollback("AppExceptionRollback");
	}
	public void  testAppExceptionRuntime() {
		counter++;
		
		em.persist(new User("John " + counter, "Doe"));
		if(true) throw new AppRuntimeException("AppRuntimeException");
	}
	public void  testAppExceptionRuntimeRollback() {
		counter++;
		
		em.persist(new User("John " + counter, "Doe"));
		if(true) throw new AppRuntimeExceptionRollback("AppRuntimeExceptionRollback");
	}
}