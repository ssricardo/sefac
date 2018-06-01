package org.rss.sefac.app;

import java.nio.file.Path;

/**
 * Base representation of a deployable to the server
 * 
 * @author Ricardo SS
 */
public abstract class Application {

	private Path sourceLocation;

	private final String name;

	
	/**
	 * @param appName: A single identifier for the application
	 */
	public Application(String appName) {
		if (appName == null || appName.isEmpty()) {
			throw new IllegalArgumentException("Application name must not be empty");
		}
		this.name = appName;
	}

	public String getName() {
		return name;
	}

	public Path getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(Path baseDir) {
		this.sourceLocation = baseDir;
	}

}
