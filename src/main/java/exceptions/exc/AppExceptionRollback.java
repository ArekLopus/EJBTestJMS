package exceptions.exc;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class AppExceptionRollback extends Exception {
	private static final long serialVersionUID = 1L;
	
	public AppExceptionRollback() {
		super();
	}
	
	public AppExceptionRollback(String msg) {
		super(msg);
	}
    
}