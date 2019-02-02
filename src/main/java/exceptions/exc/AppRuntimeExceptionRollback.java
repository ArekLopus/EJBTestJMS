package exceptions.exc;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class AppRuntimeExceptionRollback extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public AppRuntimeExceptionRollback() {
		super();
	}
	
	public AppRuntimeExceptionRollback(String msg) {
		super(msg);
	}
    
}