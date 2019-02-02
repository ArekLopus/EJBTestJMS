package exceptions.exc;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=false)
public class AppRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public AppRuntimeException() {
		super();
	}
	
	public AppRuntimeException(String msg) {
		super(msg);
	}
    
}