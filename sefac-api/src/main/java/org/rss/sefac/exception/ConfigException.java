package org.rss.sefac.exception;

/*
 * Exception which may be thrown due to misconfiguration or lack of required configuration
 */
public class ConfigException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigException() {
		super();
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}
	
}
