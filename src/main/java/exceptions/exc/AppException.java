package exceptions.exc;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=false)
public class AppException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public AppException() {
		super();
	}
	
	public AppException(String msg) {
		super(msg);
	}
    
}