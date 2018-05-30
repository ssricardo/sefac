package org.rss.sefac.exception;

/**
 * Exception that may be raised on runtime operations like starting the server
 * @author Ricardo
 */
public class ExecutionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExecutionException() {
		super();
	}

	public ExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecutionException(Throwable cause) {
		super(cause);
	}
	
}
